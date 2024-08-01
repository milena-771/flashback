package co.simplon.flashback.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.simplon.flashback.dtos.MovieDetails;
import co.simplon.flashback.dtos.MovieItem;
import co.simplon.flashback.entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

	Boolean existsByIsan(String isan);

	Boolean existsByTrailer(String trailer);

	Collection<MovieItem> findAllProjectedByOrderByReleaseYearAscTitle();

	MovieDetails findProjectedById(Long id);

	@Query("SELECT m.id FROM Movie m WHERE m.trailer = :trailer AND m.id != :movieId")
	Optional<Long> existsByTrailerForUpdate(@Param("trailer") String trailer,
			@Param("movieId") Long movieId);

	@Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :searchTitle, '%')) ORDER BY m.releaseYear")
	List<MovieItem> findMovieByTitle(@Param("searchTitle") String searchTitle);

	@Query("SELECT m FROM Movie m JOIN Direction td ON td.movie.id = m.id JOIN Director d ON d.id = td.director.id WHERE LOWER(d.lastname) LIKE LOWER(CONCAT('%', :directorLastname, '%'))")
	List<MovieItem> findMovieByDirectorLastname(
			@Param("directorLastname") String directorLastname);

	@Query("SELECT m FROM Movie m JOIN Genre g ON m.genre.id = g.id WHERE LOWER(g.genreName) LIKE LOWER(CONCAT('%', :genre, '%')) ORDER BY m.releaseYear, m.title")
	List<MovieItem> findMovieByGenre(@Param("genre") String genre);

}
