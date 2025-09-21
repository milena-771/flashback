package co.simplon.flashback.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.simplon.flashback.dtos.DirectorDetails;
import co.simplon.flashback.dtos.FavoriteAdd;
import co.simplon.flashback.dtos.MovieFavorite;
import co.simplon.flashback.dtos.MovieForSearch;
import co.simplon.flashback.entities.Favorite;
import co.simplon.flashback.entities.Movie;
import co.simplon.flashback.entities.User;
import co.simplon.flashback.repositories.DirectionRepository;
import co.simplon.flashback.repositories.FavoriteRepository;
import co.simplon.flashback.repositories.MovieRepository;
import co.simplon.flashback.repositories.UserRepository;

@Service
@Transactional(readOnly = true)
public class FavoriteServiceImpl implements FavoriteService {

	private final Logger LOG = LogManager.getLogger(FavoriteServiceImpl.class);

	private final FavoriteRepository favorites;

	private final MovieRepository movies;

	private final UserRepository users;

	private final DirectionRepository directions;

	public FavoriteServiceImpl(FavoriteRepository favorites, MovieRepository movies,
			UserRepository users, DirectionRepository directions) {
		this.favorites = favorites;
		this.movies = movies;
		this.users = users;
		this.directions = directions;
	}

	@Override
	@Transactional
	public void addFavorite(FavoriteAdd inputs) {
		try {
			LOG.info("START >>> addFavorite");
			Favorite entity = new Favorite();
			String subject = SecurityContextHolder.getContext().getAuthentication().getName();
			Long id = Long.valueOf(subject);
			User user = users.getReferenceById(id);
			Movie movie = movies.getReferenceById(inputs.movieId());
			entity.setMovie(movie);
			entity.setUser(user);
			favorites.save(entity);
		} finally {
			LOG.info("END <<< addFavorite");
		}
	}

	@Override
	@Transactional
	public void deleteFavorite(Long movieId) {
		try {
			LOG.info("START >>> deleteFavorite");
			String subject = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = Long.valueOf(subject);
			Long favoriteId = favorites.findByMovieIdAndUserId(movieId, userId);
			favorites.deleteById(favoriteId);
		} finally {
			LOG.info("END <<< deleteFavorite");
		}
	}

	@Override
	public Collection<MovieFavorite> getAllFavorites() {
		try {
			LOG.info("START >>> getAllFavorites");
			String subject = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = Long.valueOf(subject);
			return favorites.findByUserId(userId);
		} finally {
			LOG.info("END <<< getAllFavorites");
		}
	}

	@Override
	public Collection<MovieForSearch> getAllFavoritesWithDirectors() {
		try {
			LOG.info("START >>> getAllFavoritesWithDirectors");
			String subject = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = Long.valueOf(subject);
			Collection<MovieFavorite> favoritesList = favorites.findByUserId(userId);
			Collection<MovieForSearch> favoriteMoviesWithDirectors = new ArrayList<>();
			for (MovieFavorite movie : favoritesList) {
				MovieForSearch favoriteMovie = new MovieForSearch();
				Set<DirectorDetails> directors = directions.getMovieDirector(movie.getMovieId());
				favoriteMovie.setId(movie.getMovieId());
				favoriteMovie.setTitle(movie.getMovieTitle());
				favoriteMovie.setReleaseYear(movie.getMovieReleaseYear());
				favoriteMovie.setPoster(movie.getMoviePoster());
				favoriteMovie.setDirectors(directors);
				favoriteMovie.setSummary(movie.getMovieSummary());
				favoriteMoviesWithDirectors.add(favoriteMovie);
			}
			return favoriteMoviesWithDirectors;
		} finally {
			LOG.info("END <<< getAllFavoritesWithDirectors");
		}
	}
}
