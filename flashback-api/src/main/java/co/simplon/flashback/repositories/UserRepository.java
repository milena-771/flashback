package co.simplon.flashback.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.simplon.flashback.dtos.UserItem;
import co.simplon.flashback.entities.User;

public interface UserRepository
	extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    User findByEmail(String email);

    User findProjectedById(Long id);

    @Query("SELECT u FROM User u WHERE u.role.roleName ='USER' ORDER BY u.createdAt, u.lastname, u.firstname")
    Collection<UserItem> getAllUsers();

}
