package co.simplon.flashback.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import co.simplon.flashback.dtos.DirectorDetails;
import co.simplon.flashback.dtos.GenreDetails;
import co.simplon.flashback.dtos.Labels;
import co.simplon.flashback.dtos.MovieCreate;
import co.simplon.flashback.dtos.MovieDetails;
import co.simplon.flashback.dtos.MovieFavorite;
import co.simplon.flashback.dtos.MovieForSearch;
import co.simplon.flashback.dtos.MovieForUpdate;
import co.simplon.flashback.dtos.MovieItem;
import co.simplon.flashback.dtos.MoviePoster;
import co.simplon.flashback.dtos.MovieUpdate;
import co.simplon.flashback.dtos.MoviesForSearchAndFavorites;
import co.simplon.flashback.entities.Direction;
import co.simplon.flashback.entities.Director;
import co.simplon.flashback.entities.Genre;
import co.simplon.flashback.entities.Movie;
import co.simplon.flashback.entities.Retrospective;
import co.simplon.flashback.errors.FlashbackException;
import co.simplon.flashback.repositories.DirectionRepository;
import co.simplon.flashback.repositories.DirectorRepository;
import co.simplon.flashback.repositories.FavoriteRepository;
import co.simplon.flashback.repositories.GenreRepository;
import co.simplon.flashback.repositories.MovieRepository;
import co.simplon.flashback.repositories.ParticipantRepository;
import co.simplon.flashback.repositories.ProgramRepository;
import co.simplon.flashback.repositories.RetrospectiveRepository;

@Service
@Transactional(readOnly = true)
public class MovieServiceImpl implements MovieService {

    @Value("${flashback-api.uploads.location}")
    private String uploadDir;

    private final MessageSource messageSource;

    private final MovieRepository movies;

    private final GenreRepository genres;

    private final DirectorRepository directors;

    private final DirectionRepository directions;

    private final FavoriteRepository favorites;

    private final ProgramRepository containor;

    private final ParticipantRepository participants;

    private final RetrospectiveRepository retrospectives;

    public MovieServiceImpl(MovieRepository movies,
	    GenreRepository genres,
	    DirectorRepository directors,
	    DirectionRepository directions,
	    FavoriteRepository favorites,
	    ProgramRepository containor,
	    ParticipantRepository participants,
	    RetrospectiveRepository retrospectives,
	    MessageSource messageSource) {
	this.movies = movies;
	this.genres = genres;
	this.directors = directors;
	this.directions = directions;
	this.favorites = favorites;
	this.containor = containor;
	this.participants = participants;
	this.retrospectives = retrospectives;
	this.messageSource = messageSource;
    }

    @Override
    @Transactional
    public void create(MovieCreate inputs) {
	Movie entity = new Movie();
	entity.setIsan(inputs.isan());
	entity.setTitle(inputs.title());
	entity.setReleaseYear(inputs.releaseYear());
	MultipartFile image = inputs.poster();
	String posterName = rename(image);
	entity.setPoster(posterName);
	store(image, posterName);
	entity.setTrailer(inputs.trailer());
	entity.setSummary(inputs.summary());
	Genre genre = genres
		.getReferenceById(inputs.genreId());
	entity.setGenre(genre);
	Movie movie = movies.save(entity);
	for (Long id : inputs.directorId()) {
	    Direction movieDirector = new Direction();
	    Director director = directors
		    .getReferenceById(id);
	    movieDirector.setDirector(director);
	    movieDirector.setMovie(movie);
	    directions.save(movieDirector);
	}
    }

    public String rename(MultipartFile poster) {
	String baseName = UUID.randomUUID().toString();
	String extention = StringUtils.getFilenameExtension(
		poster.getOriginalFilename());
	String posterName = baseName + "." + extention;
	return posterName;
    }

    private void store(MultipartFile image,
	    String posterName) {
	Path uploadPath = Paths.get(uploadDir);
	Path target = uploadPath.resolve(posterName);
	try (InputStream in = image.getInputStream()) {
	    Files.copy(in, target,
		    StandardCopyOption.REPLACE_EXISTING);
	} catch (IOException ex) {
	    throw new RuntimeException(ex);
	}
    }

    @Override
    public Collection<MovieItem> getAllForEdit() {
	return movies
		.findAllProjectedByOrderByReleaseYearAscTitle();
    }

    @Override
    public Boolean existsByIsan(String isan) {
	return movies.existsByIsan(isan);
    }

    @Override
    public Boolean existsByTrailer(String trailer) {
	return movies.existsByTrailer(trailer);
    }

    @Override
    public Labels getAllLabels() {
	Collection<GenreDetails> allGenres = genres
		.findAllProjectedByOrderByGenreName();
	Collection<DirectorDetails> allDirectors = directors
		.findAllProjectedByOrderByLastnameAscFirstname();
	Collection<Labels> labelsList = new HashSet<>();
	Labels labels = new Labels();
	labels.setAllGenres(allGenres);
	labels.setAllDirectors(allDirectors);
	return labels;
    }

    @Override
    @Transactional
    public void delete(Long movieId) {
	if (movies.existsById(movieId)) {
	    directions.deleteByMovieId(movieId);
	    favorites.deleteByMovieId(movieId);
	    if (containor.existsByMovieId(movieId)) {
		Set<Retrospective> retros = containor
			.findByMovieId(movieId);
		containor.deleteByMovieId(movieId);
		for (Retrospective retro : retros) {
		    if (retro.getMoviesNumber() > 1) {
			retro.setMoviesNumber(
				retro.getMoviesNumber()
					- 1);
			retrospectives.save(retro);
		    } else {
			participants
				.deleteByRetrospectiveId(
					retro.getId());
			retrospectives
				.deleteById(retro.getId());
		    }
		}
	    }
	    Movie movie = movies.findById(movieId).get();
	    if (movie.getPoster() != null) {
		Path poster = Paths.get(uploadDir,
			movie.getPoster());
		poster.toFile().delete();
	    }
	    movies.deleteById(movieId);
	} else {
	    throw new FlashbackException(
		    messageSource.getMessage(
			    "error.movie.remove.admin",
			    null, Locale.getDefault()),
		    HttpStatus.BAD_REQUEST.name());
	}

    }

    @Override
    public MovieForUpdate forUpdate(Long id) {
	MovieDetails movie = movies.findProjectedById(id);
	Set<DirectorDetails> directorsDetails = directions
		.getMovieDirector(id);
	MovieForUpdate movieInfos = new MovieForUpdate();
	movieInfos.setMovieDetails(movie);
	movieInfos.setDirectorDetails(directorsDetails);
	return movieInfos;
    }

    @Override
    @Transactional
    public MoviePoster update(Long id, MovieUpdate inputs) {
	Movie entity = movies.findById(id).get();
	MoviePoster newPoster = new MoviePoster();
	if (inputs.poster() != null) {
	    Path oldPoster = Paths.get(uploadDir,
		    entity.getPoster());
	    MultipartFile poster = inputs.poster();
	    String posterName = rename(poster);
	    entity.setPoster(posterName);
	    newPoster.setPoster(posterName);
	    store(poster, posterName);
	    oldPoster.toFile().delete();
	}
	entity.setTitle(inputs.title());
	entity.setReleaseYear(inputs.releaseYear());
	entity.setTrailer(inputs.trailer());
	entity.setSummary(inputs.summary());
	Genre genre = genres
		.getReferenceById(inputs.genreId());
	entity.setGenre(genre);
	movies.save(entity);
	directions.deleteByMovieId(id);
	for (Long directorId : inputs.directorId()) {
	    Direction updateDirection = directions
		    .findByMovieIdAndDirectorId(id,
			    directorId);
	    Direction newDirection = new Direction();
	    Director director = directors
		    .getReferenceById(directorId);
	    newDirection.setDirector(director);
	    newDirection.setMovie(entity);
	    directions.save(newDirection);
	}
	return newPoster;

    }

    @Override
    public Optional<Long> existsByTrailerForUpdate(
	    MovieUpdate inputs) {
	return movies.existsByTrailerForUpdate(
		inputs.trailer(), inputs.id());
    }

    @Override
    public MoviesForSearchAndFavorites getAllForSearch() {
	String subject = SecurityContextHolder.getContext()
		.getAuthentication().getName();
	Long userId = Long.valueOf(subject);
	Collection<MovieItem> allMovies = movies
		.findAllProjectedByOrderByReleaseYearAscTitle();
	Collection<MovieForSearch> listMovies = new ArrayList<>();
	for (MovieItem movie : allMovies) {
	    MovieForSearch searchMovie = new MovieForSearch();
	    Set<DirectorDetails> directors = directions
		    .getMovieDirector(movie.getId());
	    searchMovie.setId(movie.getId());
	    searchMovie.setTitle(movie.getTitle());
	    searchMovie
		    .setReleaseYear(movie.getReleaseYear());
	    searchMovie.setPoster(movie.getPoster());
	    searchMovie.setDirectors(directors);
	    listMovies.add(searchMovie);
	}
	Collection<MovieFavorite> favoritesList = favorites
		.findByUserId(userId);
	MoviesForSearchAndFavorites initMoviesAndFavorites = new MoviesForSearchAndFavorites();
	initMoviesAndFavorites.setFavorites(favoritesList);
	initMoviesAndFavorites.setMovies(listMovies);
	return initMoviesAndFavorites;
    }

}
