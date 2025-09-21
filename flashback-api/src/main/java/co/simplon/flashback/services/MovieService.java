package co.simplon.flashback.services;

import java.util.Optional;

import org.springframework.data.domain.Page;

import co.simplon.flashback.dtos.Labels;
import co.simplon.flashback.dtos.MovieCreate;
import co.simplon.flashback.dtos.MovieForSearch;
import co.simplon.flashback.dtos.MovieForUpdate;
import co.simplon.flashback.dtos.MovieItem;
import co.simplon.flashback.dtos.MoviePoster;
import co.simplon.flashback.dtos.MovieUpdate;
import co.simplon.flashback.dtos.MoviesForSearchAndFavorites;

public interface MovieService {

	void createMovie(MovieCreate inputs);

	Boolean existsByIsan(String isan);

	Boolean existsByTrailer(String trailer);

	Page<MovieItem> getAllMoviesForEdit(String title, int page, int size);

	Labels getAllLabels();

	void deleteMovie(Long id);

	MovieForUpdate getMovieforUpdate(Long id);

	MoviePoster updateMovie(Long id, MovieUpdate inputs);

	Optional<Long> existsByTrailerForUpdate(MovieUpdate inputs);

	MoviesForSearchAndFavorites getAllMoviesForSearch(int page, int size);

	Page<MovieForSearch> searchMovieByTitle(String title, int page, int size);

	Page<MovieForSearch> searchMoviesByDirectorLastname(String lastname, int page, int size);

	Page<MovieForSearch> searchMoviesByGenre(String genre, int page, int size);
}
