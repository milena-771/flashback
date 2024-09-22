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

	void create(MovieCreate inputs);

	Boolean existsByIsan(String isan);

	Boolean existsByTrailer(String trailer);

	Page<MovieItem> getAllForEdit(String title, int page, int size);

	Labels getAllLabels();

	void delete(Long id);

	MovieForUpdate forUpdate(Long id);

	MoviePoster update(Long id, MovieUpdate inputs);

	Optional<Long> existsByTrailerForUpdate(MovieUpdate inputs);

	MoviesForSearchAndFavorites getAllForSearch(int page, int size);

	Page<MovieForSearch> searchByTitle(String title, int page, int size);

	Page<MovieForSearch> searchByDirectorLastname(String lastname, int page,
			int size);

	Page<MovieForSearch> searchByGenre(String genre, int page, int size);
}
