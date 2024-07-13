package co.simplon.flashback.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import co.simplon.flashback.dtos.DirectorDetails;
import co.simplon.flashback.entities.Director;

public interface DirectorRepository
	extends JpaRepository<Director, Long> {

    Collection<DirectorDetails> findAllProjectedByOrderByLastnameAscFirstname();

}
