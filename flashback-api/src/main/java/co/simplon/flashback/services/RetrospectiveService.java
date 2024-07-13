package co.simplon.flashback.services;

import java.util.Collection;
import java.util.Optional;

import co.simplon.flashback.dtos.AdminRetroDetailsWithMovies;
import co.simplon.flashback.dtos.AdminRetroItem;
import co.simplon.flashback.dtos.DeviceDetails;
import co.simplon.flashback.dtos.FavoriteAndLabelsAndRetroDetailsForUpdate;
import co.simplon.flashback.dtos.OrgaRetroForUpdate;
import co.simplon.flashback.dtos.ParticipantRetroForUpdate;
import co.simplon.flashback.dtos.RetroItem;
import co.simplon.flashback.dtos.RetroItemsAsOrgaAndParticipant;
import co.simplon.flashback.dtos.RetrospectiveCreate;
import co.simplon.flashback.dtos.RetrospectiveUpdate;

public interface RetrospectiveService {

    void create(RetrospectiveCreate inputs);

    Boolean existsByRetroName(String name);

    Collection<DeviceDetails> getAllDeviceLabels();

    RetroItemsAsOrgaAndParticipant getAllRetroAsOrgaAndParticipant();

    Collection<RetroItem> getAllRetroToCome();

    void addParticipant(Long retrospectiveId);

    void removeParticipant(Long retrospectiveId);

    OrgaRetroForUpdate getOrgaRetroDetails(
	    Long retrospectiveId);

    ParticipantRetroForUpdate getParticipantRetroDetails(
	    Long retrospectiveId);

    ParticipantRetroForUpdate getRetroToComeDetails(
	    Long retrospectiveId);

    void deleteRetrospectiveByOrganizer(
	    Long retrospectiveId);

    void deleteRetrospectiveByAdmin(Long retrospectiveId);

    FavoriteAndLabelsAndRetroDetailsForUpdate getRetroDetailsForUpdate(
	    Long retrospectiveId);

    void updateRetrospective(Long retrospectiveId,
	    RetrospectiveUpdate inputs);

    Optional<Long> existsByRetrospectiveNameForUpdate(
	    RetrospectiveUpdate inputs);

    Collection<AdminRetroItem> getAllRetrospectives();

    AdminRetroDetailsWithMovies getAdminRetroDetails(
	    Long retrospectiveId);

}
