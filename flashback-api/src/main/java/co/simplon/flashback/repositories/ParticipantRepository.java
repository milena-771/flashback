package co.simplon.flashback.repositories;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.simplon.flashback.dtos.RetroItem;
import co.simplon.flashback.entities.Participant;
import co.simplon.flashback.entities.Retrospective;

public interface ParticipantRepository
	extends JpaRepository<Participant, Long> {

    @Query("SELECT p.id FROM Participant p WHERE p.retrospective.id = :retroId AND p.user.id = :userId")
    Long findByUserIdAndRetroId(
	    @Param("retroId") Long retroId,
	    @Param("userId") Long userId);

    @Query("""
    	SELECT r FROM Retrospective r JOIN Participant p ON p.retrospective.id = r.id
    	WHERE p.user.id = :userId AND (r.startDate >= :today OR r.endDate >= :today)
    	""")
    Set<RetroItem> findByUserIdAndDate(
	    @Param("userId") Long userId,
	    @Param("today") LocalDate today);

    @Query("SELECT p.id FROM Participant p WHERE p.retrospective.id = :retroId AND p.user.id = :userId")
    Optional<Long> existsByUserIdAndRetroId(
	    @Param("retroId") Long retroId,
	    @Param("userId") Long userId);

    void deleteByRetrospectiveId(Long retroId);

    Boolean existsByUserId(Long userId);

    void deleteByUserId(Long userId);

    @Query("SELECT r FROM Retrospective r JOIN Participant p ON p.retrospective.id = r.id WHERE p.user.id = :userId")
    Set<Retrospective> findByUserId(
	    @Param("userId") Long userId);

}
