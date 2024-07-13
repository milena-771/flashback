package co.simplon.flashback.repositories;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.simplon.flashback.dtos.MovieDetails;
import co.simplon.flashback.dtos.MovieItem;
import co.simplon.flashback.entities.Movie;

public interface MovieRepository
	extends JpaRepository<Movie, Long> {

    Boolean existsByIsan(String isan);

    Boolean existsByTrailer(String trailer);

    Collection<MovieItem> findAllProjectedByOrderByReleaseYearAscTitle();

    MovieDetails findProjectedById(Long id);

    @Query("SELECT m.id FROM Movie m WHERE m.trailer = :trailer AND m.id != :movieId")
    Optional<Long> existsByTrailerForUpdate(
	    @Param("trailer") String trailer,
	    @Param("movieId") Long movieId);

}
