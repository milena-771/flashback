package co.simplon.flashback.repositories;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.simplon.flashback.dtos.AdminRetroItem;
import co.simplon.flashback.dtos.RetroItem;
import co.simplon.flashback.entities.Retrospective;

public interface RetrospectiveRepository
	extends JpaRepository<Retrospective, Long> {

    Boolean existsByRetrospectiveName(String name);

    @Query("SELECT r FROM Retrospective r WHERE r.organizer.id = :userId AND (r.startDate >= :today OR r.endDate >= :today)")
    Collection<RetroItem> findRetroByOrganizerAndByDate(
	    @Param("userId") Long userId,
	    @Param("today") LocalDate today);

    @Query("SELECT r FROM Retrospective r WHERE r.organizer.id != :userId AND (r.startDate >= :today OR r.endDate >= :today)")
    Collection<RetroItem> findRetroToCome(
	    @Param("userId") Long userId,
	    @Param("today") LocalDate today);

    @Query("SELECT r.id FROM Retrospective r WHERE r.retrospectiveName = :retrospectiveName AND r.id != :retroId")
    Optional<Long> existsByRetrospectiveNameForUpdate(
	    @Param("retrospectiveName") String retrospectiveName,
	    @Param("retroId") Long retroId);

    Collection<AdminRetroItem> findAllProjectedByOrderByStartDateAscEndDateAscRetrospectiveName();

    void deleteByOrganizerId(Long userId);

    Boolean existsByOrganizerId(Long userId);

    Set<Retrospective> findByOrganizerId(Long userId);

}
