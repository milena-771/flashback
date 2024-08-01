package co.simplon.flashback.services;

import java.util.Collection;
import java.util.Optional;

import co.simplon.flashback.dtos.Labels;
import co.simplon.flashback.dtos.MovieCreate;
import co.simplon.flashback.dtos.MovieForSearch;
import co.simplon.flashback.dtos.MovieForUpdate;
import co.simplon.flashback.dtos.MovieItem;
import co.simplon.flashback.dtos.MoviePoster;
import co.simplon.flashback.dtos.MovieUpdate;
import co.simplon.flashback.dtos.MoviesForSearchAndFavorites;

public interface MovieService {

	void create(MovieCreate inputs);

	Boolean existsByIsan(String isan);

	Boolean existsByTrailer(String trailer);

	Collection<MovieItem> getAllForEdit();

	Labels getAllLabels();

	void delete(Long id);

	MovieForUpdate forUpdate(Long id);

	MoviePoster update(Long id, MovieUpdate inputs);

	Optional<Long> existsByTrailerForUpdate(MovieUpdate inputs);

	MoviesForSearchAndFavorites getAllForSearch();

	Collection<MovieForSearch> searchByTitle(String title);

	Collection<MovieForSearch> searchByDirectorLastname(String lastname);

	Collection<MovieForSearch> searchByGenre(String genre);
}
