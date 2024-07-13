package co.simplon.flashback.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.simplon.flashback.dtos.DirectorDetails;
import co.simplon.flashback.entities.Direction;

public interface DirectionRepository
	extends JpaRepository<Direction, Long> {

    @Query("SELECT a FROM Director a JOIN Direction b ON b.director.id = a.id WHERE b.movie.id = :movieId")
    Set<DirectorDetails> getMovieDirector(
	    @Param("movieId") Long movieId);

    void deleteByMovieId(Long movieId);

    @Query("SELECT b FROM Direction b WHERE b.movie.id = :movieId AND b.director.id = :directorId")
    Direction findByMovieIdAndDirectorId(
	    @Param("movieId") Long movieId,
	    @Param("directorId") Long directorId);
}
