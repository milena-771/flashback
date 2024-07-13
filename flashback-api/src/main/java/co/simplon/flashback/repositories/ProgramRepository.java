package co.simplon.flashback.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.simplon.flashback.dtos.MovieItem;
import co.simplon.flashback.entities.Program;
import co.simplon.flashback.entities.Retrospective;

public interface ProgramRepository
	extends JpaRepository<Program, Long> {

    @Query("SELECT m FROM Movie m JOIN Program p ON p.movie.id = m.id WHERE p.retrospective.id = :retroId")
    Set<MovieItem> findByRetrospectiveId(
	    @Param("retroId") Long retroId);

    @Query("SELECT p FROM Program p WHERE p.movie.id = :movieId AND p.retrospective.id = :retroId")
    Program findByMovieIdAndRetrospectiveId(
	    @Param("movieId") Long movieId,
	    @Param("retroId") Long retroId);

    Boolean existsByMovieId(Long movieId);

    @Query("SELECT r FROM Retrospective r JOIN Program p ON p.retrospective.id = r.id WHERE p.movie.id = :movieId")
    Set<Retrospective> findByMovieId(
	    @Param("movieId") Long movieId);

    void deleteByMovieId(Long movieId);

    void deleteByRetrospectiveId(Long retroId);

}
