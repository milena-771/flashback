package co.simplon.flashback.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.simplon.flashback.entities.Role;

public interface RoleRepository
	extends JpaRepository<Role, Long> {

}
