package co.simplon.flashback.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.simplon.flashback.dtos.MovieFavorite;
import co.simplon.flashback.entities.Favorite;

public interface FavoriteRepository
	extends JpaRepository<Favorite, Long> {

    @Query("SELECT f.id FROM Favorite f WHERE f.movie.id = :movieId AND f.user.id = :userId")
    Long findByMovieIdAndUserId(
	    @Param("movieId") Long movieId,
	    @Param("userId") Long userId);

    Collection<MovieFavorite> findByUserId(Long userId);

    void deleteByMovieId(Long movieId);

    void deleteByUserId(Long userId);
}
