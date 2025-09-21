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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import co.simplon.flashback.errors.NotFoundException;
import co.simplon.flashback.errors.PosterUploadException;
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

	private final Logger LOG = LogManager.getLogger(MovieServiceImpl.class);

	@Value("${flashback-api.uploads.location}")
	private String uploadDir;

	private final MovieRepository movies;

	private final GenreRepository genres;

	private final DirectorRepository directors;

	private final DirectionRepository directions;

	private final FavoriteRepository favorites;

	private final ProgramRepository containor;

	private final ParticipantRepository participants;

	private final RetrospectiveRepository retrospectives;

	public MovieServiceImpl(MovieRepository movies, GenreRepository genres,
			DirectorRepository directors, DirectionRepository directions,
			FavoriteRepository favorites, ProgramRepository containor,
			ParticipantRepository participants, RetrospectiveRepository retrospectives) {
		this.movies = movies;
		this.genres = genres;
		this.directors = directors;
		this.directions = directions;
		this.favorites = favorites;
		this.containor = containor;
		this.participants = participants;
		this.retrospectives = retrospectives;
	}

	@Override
	@Transactional
	public void createMovie(MovieCreate inputs) {
		try {
			LOG.info("START >>> createMovie");
			Movie entity = new Movie();
			entity.setIsan(inputs.isan());
			entity.setTitle(inputs.title());
			entity.setReleaseYear(inputs.releaseYear());
			MultipartFile image = inputs.poster();
			String posterName = renamePoster(image);
			entity.setPoster(posterName);
			storePoster(image, posterName);
			entity.setTrailer(inputs.trailer());
			entity.setSummary(inputs.summary());
			Genre genre = genres.getReferenceById(inputs.genreId());
			entity.setGenre(genre);
			Movie movie = movies.save(entity);
			for (Long id : inputs.directorId()) {
				Direction movieDirector = new Direction();
				Director director = directors.getReferenceById(id);
				movieDirector.setDirector(director);
				movieDirector.setMovie(movie);
				directions.save(movieDirector);
			}
		} finally {
			LOG.info("END <<< createMovie");
		}
	}

	public String renamePoster(MultipartFile poster) {
		try {
			LOG.info("START >>> renamePoster");
			String baseName = UUID.randomUUID().toString();
			String extention = StringUtils.getFilenameExtension(poster.getOriginalFilename());
			String posterName = baseName + "." + extention;
			return posterName;
		} finally {
			LOG.info("END <<< renamePoster");
		}
	}

	private void storePoster(MultipartFile image, String posterName) {
		try {
			LOG.info("START >>> storePoster");
			Path uploadPath = Paths.get(uploadDir);
			Path target = uploadPath.resolve(posterName);
			try (InputStream in = image.getInputStream()) {
				Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException ex) {
				LOG.error("Failed to store movie poster: {}", posterName);
				throw new PosterUploadException("POSTER_UPLOAD", "Failed to store movie poster.");
			}
		} finally {
			LOG.info("END <<< storePoster");
		}
	}

	@Override
	public Page<MovieItem> getAllMoviesForEdit(String title, int page, int size) {
		PageRequest pageRequest = PageRequest.of(page - 1, size);
		try {
			LOG.info("START >>> getAllMoviesForEdit");
			if (title.isEmpty()) {
				return movies.findAllProjectedByOrderByReleaseYearAscTitle(pageRequest);
			} else {
				return movies.findMovieByTitleForEdit(title, pageRequest);
			}
		} finally {
			LOG.info("END <<< getAllMoviesForEdit");
		}
	}

	@Override
	public Boolean existsByIsan(String isan) {
		try {
			LOG.info("START >>> existsByIsan");
			return movies.existsByIsan(isan);
		} finally {
			LOG.info("END <<< existsByIsan");
		}
	}

	@Override
	public Boolean existsByTrailer(String trailer) {
		try {
			LOG.info("START >>> existsByTrailer");
			return movies.existsByTrailer(trailer);
		} finally {
			LOG.info("END <<< existsByTrailer");
		}
	}

	@Override
	public Labels getAllLabels() {
		try {
			LOG.info("START >>> getAllLabels");
			Collection<GenreDetails> allGenres = genres.findAllProjectedByOrderByGenreName();
			Collection<DirectorDetails> allDirectors = directors
					.findAllProjectedByOrderByLastnameAscFirstname();
			Collection<Labels> labelsList = new HashSet<>();
			Labels labels = new Labels();
			labels.setAllGenres(allGenres);
			labels.setAllDirectors(allDirectors);
			return labels;
		} finally {
			LOG.info("END <<< getAllLabels");
		}
	}

	@Override
	@Transactional
	public void deleteMovie(Long movieId) {
		try {
			LOG.info("START >>> deleteMovie");
			directions.deleteByMovieId(movieId);
			favorites.deleteByMovieId(movieId);
			if (containor.existsByMovieId(movieId)) {
				Set<Retrospective> retros = containor.findByMovieId(movieId);
				containor.deleteByMovieId(movieId);
				for (Retrospective retro : retros) {
					if (retro.getMoviesNumber() > 1) {
						retro.setMoviesNumber(retro.getMoviesNumber() - 1);
						retrospectives.save(retro);
					} else {
						participants.deleteByRetrospectiveId(retro.getId());
						retrospectives.deleteById(retro.getId());
					}
				}
			}
			Movie movie = movies.findById(movieId).orElseThrow(() -> {
				LOG.error("Movie not found in deleteMovie: {}", movieId);
				return new NotFoundException("MOVIE_NOT_FOUND",
						"Movie not found, deletion could not be completed.");
			});
			if (movie.getPoster() != null) {
				try {
					Files.deleteIfExists(Paths.get(uploadDir, movie.getPoster()));
				} catch (IOException e) {
					LOG.warn("Failed to delete poster for movie {}: {}", movieId, e.getMessage());
				}
			}
			movies.deleteById(movieId);
		} finally {
			LOG.info("END <<< deleteMovie");
		}
	}

	@Override
	public MovieForUpdate getMovieforUpdate(Long id) {
		try {
			LOG.info("START >>> getMovieforUpdate");
			MovieDetails movie = movies.findProjectedById(id);
			Set<DirectorDetails> directorsDetails = directions.getMovieDirector(id);
			MovieForUpdate movieInfos = new MovieForUpdate();
			movieInfos.setMovieDetails(movie);
			movieInfos.setDirectorDetails(directorsDetails);
			return movieInfos;
		} finally {
			LOG.info("END <<< getMovieforUpdate");
		}
	}

	@Override
	@Transactional
	public MoviePoster updateMovie(Long id, MovieUpdate inputs) {
		try {
			LOG.info("START >>> updateMovie");
			Movie entity = movies.findById(id).orElseThrow(() -> {
				LOG.error("Movie not found in updateMovie: {}", id);
				return new NotFoundException("MOVIE_NOT_FOUND",
						"Movie not found, update could not be completed.");
			});
			;
			MoviePoster newPoster = new MoviePoster();
			if (inputs.poster() != null) {
				Path oldPoster = Paths.get(uploadDir, entity.getPoster());
				MultipartFile poster = inputs.poster();
				String posterName = renamePoster(poster);
				entity.setPoster(posterName);
				newPoster.setPoster(posterName);
				storePoster(poster, posterName);
				oldPoster.toFile().delete();
			}
			entity.setTitle(inputs.title());
			entity.setReleaseYear(inputs.releaseYear());
			entity.setTrailer(inputs.trailer());
			entity.setSummary(inputs.summary());
			Genre genre = genres.getReferenceById(inputs.genreId());
			entity.setGenre(genre);
			movies.save(entity);
			directions.deleteByMovieId(id);
			for (Long directorId : inputs.directorId()) {
				Direction updateDirection = directions.findByMovieIdAndDirectorId(id, directorId);
				Direction newDirection = new Direction();
				Director director = directors.getReferenceById(directorId);
				newDirection.setDirector(director);
				newDirection.setMovie(entity);
				directions.save(newDirection);
			}
			return newPoster;
		} finally {
			LOG.info("END <<< updateMovie");
		}
	}

	@Override
	public Optional<Long> existsByTrailerForUpdate(MovieUpdate inputs) {
		try {
			LOG.info("START >>> existsByTrailerForUpdate");
			return movies.existsByTrailerForUpdate(inputs.trailer(), inputs.id());
		} finally {
			LOG.info("END <<< existsByTrailerForUpdate");
		}
	}

	@Override
	public MoviesForSearchAndFavorites getAllMoviesForSearch(int page, int size) {
		try {
			LOG.info("START >>> getAllMoviesForSearch");
			String subject = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = Long.valueOf(subject);
			Collection<MovieItem> allMovies = movies.findAllProjectedByOrderByReleaseYearAscTitle();
			PageRequest pageRequest = PageRequest.of(page - 1, size);
			Page<MovieForSearch> allMoviesWithDirectors = getMoviesListWithDirectorsByPage(
					allMovies, pageRequest);
			Collection<MovieFavorite> favoritesList = favorites.findByUserId(userId);
			MoviesForSearchAndFavorites initMoviesAndFavorites = new MoviesForSearchAndFavorites();
			initMoviesAndFavorites.setFavorites(favoritesList);
			initMoviesAndFavorites.setMovies(allMoviesWithDirectors);
			return initMoviesAndFavorites;
		} finally {
			LOG.info("END <<< getAllMoviesForSearch");
		}
	}

	private Page<MovieForSearch> getMoviesListWithDirectorsByPage(Collection<MovieItem> moviesList,
			PageRequest pageRequest) {
		try {
			LOG.info("START >>> getMoviesListWithDirectorsByPage");
			List<MovieForSearch> moviesWithDirectors = new ArrayList<>();
			for (MovieItem movie : moviesList) {
				MovieForSearch searchMovie = new MovieForSearch();
				Set<DirectorDetails> directors = directions.getMovieDirector(movie.getId());
				searchMovie.setId(movie.getId());
				searchMovie.setTitle(movie.getTitle());
				searchMovie.setReleaseYear(movie.getReleaseYear());
				searchMovie.setPoster(movie.getPoster());
				searchMovie.setDirectors(directors);
				searchMovie.setGenreName(movie.getGenre().getGenreName());
				moviesWithDirectors.add(searchMovie);
			}
			int start = (int) pageRequest.getOffset();
			int end = Math.min((start + pageRequest.getPageSize()), moviesWithDirectors.size());
			List<MovieForSearch> pageContent = moviesWithDirectors.subList(start, end);
			return new PageImpl<MovieForSearch>(pageContent, pageRequest,
					moviesWithDirectors.size());
		} finally {
			LOG.info("END <<< getMoviesListWithDirectorsByPage");
		}
	}

	@Override
	public Page<MovieForSearch> searchMovieByTitle(String title, int page, int size) {
		try {
			LOG.info("START >>> searchMovieByTitle");
			PageRequest pageRequest = PageRequest.of(page - 1, size);
			Collection<MovieItem> moviesFound = movies.findMovieByTitle(title);
			Page<MovieForSearch> moviesWithDirectors = getMoviesListWithDirectorsByPage(moviesFound,
					pageRequest);
			return moviesWithDirectors;
		} finally {
			LOG.info("END <<< searchMovieByTitle");
		}
	}

	@Override
	public Page<MovieForSearch> searchMoviesByDirectorLastname(String lastname, int page,
			int size) {
		try {
			LOG.info("START >>> searchMoviesByDirectorLastname");
			PageRequest pageRequest = PageRequest.of(page - 1, size);
			Collection<MovieItem> moviesFound = movies.findMovieByDirectorLastname(lastname);
			Page<MovieForSearch> moviesWithDirectors = getMoviesListWithDirectorsByPage(moviesFound,
					pageRequest);
			return moviesWithDirectors;
		} finally {
			LOG.info("END <<< searchMoviesByDirectorLastname");
		}
	}

	@Override
	public Page<MovieForSearch> searchMoviesByGenre(String genre, int page, int size) {
		try {
			LOG.info("START >>> searchMoviesByGenre");
			PageRequest pageRequest = PageRequest.of(page - 1, size);
			Collection<MovieItem> moviesFound = movies.findMovieByGenre(genre);
			Page<MovieForSearch> moviesWithDirectors = getMoviesListWithDirectorsByPage(moviesFound,
					pageRequest);
			return moviesWithDirectors;
		} finally {
			LOG.info("END <<< searchMoviesByGenre");
		}
	}

}
