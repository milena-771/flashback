package co.simplon.flashback.controllers;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.simplon.flashback.dtos.DeviceDetails;
import co.simplon.flashback.dtos.FavoriteAndLabelsAndRetroDetailsForUpdate;
import co.simplon.flashback.dtos.FavoritesAndLabels;
import co.simplon.flashback.dtos.MovieForSearch;
import co.simplon.flashback.dtos.OrgaRetroForUpdate;
import co.simplon.flashback.dtos.ParticipantRetroForUpdate;
import co.simplon.flashback.dtos.RetroItem;
import co.simplon.flashback.dtos.RetroItemsAsOrgaAndParticipant;
import co.simplon.flashback.dtos.RetrospectiveCreate;
import co.simplon.flashback.dtos.RetrospectiveUpdate;
import co.simplon.flashback.services.FavoriteService;
import co.simplon.flashback.services.RetrospectiveService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/retrospectives")
public class RetrospectiveController {

    private final RetrospectiveService retroService;

    private final FavoriteService favoriteService;

    public RetrospectiveController(
	    RetrospectiveService retroService,
	    FavoriteService favoriteService) {
	this.retroService = retroService;
	this.favoriteService = favoriteService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void post(
	    @RequestBody @Valid RetrospectiveCreate inputs) {
	retroService.create(inputs);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRetrospectiveByOrganizer(
	    @PathVariable("id") Long retrospectiveId) {
	retroService.deleteRetrospectiveByOrganizer(
		retrospectiveId);
    }

    @GetMapping("/{id}/for-update")
    public FavoriteAndLabelsAndRetroDetailsForUpdate getRetroDetailsForUpdate(
	    @PathVariable("id") Long retrospectiveId) {
	return retroService
		.getRetroDetailsForUpdate(retrospectiveId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRetrospective(
	    @PathVariable("id") Long id,
	    @RequestBody @Valid RetrospectiveUpdate inputs) {
	retroService.updateRetrospective(id, inputs);
    }

    @GetMapping("/labels")
    public FavoritesAndLabels getAllLabels() {
	Collection<MovieForSearch> favorites = favoriteService
		.getAllFavoritesWithDirectors();
	Collection<DeviceDetails> devices = retroService
		.getAllDeviceLabels();
	FavoritesAndLabels labels = new FavoritesAndLabels();
	labels.setFavorites(favorites);
	labels.setDevices(devices);
	return labels;
    }

    @GetMapping("/planning")
    public RetroItemsAsOrgaAndParticipant getAllRetroAsOrgaAndParticipant() {
	return retroService
		.getAllRetroAsOrgaAndParticipant();
    }

    @GetMapping("/to-come")
    public Collection<RetroItem> getAllRetroToCome() {
	return retroService.getAllRetroToCome();
    }

    @PutMapping("/{id}/add-participant")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addParticipant(
	    @PathVariable("id") Long retrospectiveId) {
	retroService.addParticipant(retrospectiveId);
    }

    @DeleteMapping("/{id}/remove-participant")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeParticipant(
	    @PathVariable("id") Long retrospectiveId) {
	retroService.removeParticipant(retrospectiveId);
    }

    @GetMapping("/{id}/orga-details")
    public OrgaRetroForUpdate getOrgaRetroDetails(
	    @PathVariable("id") Long retroId) {
	return retroService.getOrgaRetroDetails(retroId);
    }

    @GetMapping("/{id}/participant-details")
    public ParticipantRetroForUpdate getParticipantRetroDetails(
	    @PathVariable("id") Long retroId) {
	return retroService
		.getParticipantRetroDetails(retroId);
    }

    @GetMapping("/{id}/retro-details")
    public ParticipantRetroForUpdate getRetroDetails(
	    @PathVariable("id") Long retroId) {
	return retroService.getRetroToComeDetails(retroId);
    }

}
