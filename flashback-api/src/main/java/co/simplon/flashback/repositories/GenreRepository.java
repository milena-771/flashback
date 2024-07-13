package co.simplon.flashback.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import co.simplon.flashback.dtos.GenreDetails;
import co.simplon.flashback.entities.Genre;

public interface GenreRepository
	extends JpaRepository<Genre, Long> {

    Collection<GenreDetails> findAllProjectedByOrderByGenreName();

}
