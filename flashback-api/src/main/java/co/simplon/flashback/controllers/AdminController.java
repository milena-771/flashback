package co.simplon.flashback.controllers;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.simplon.flashback.dtos.AdminRetroDetailsWithMovies;
import co.simplon.flashback.dtos.AdminRetroItem;
import co.simplon.flashback.dtos.UserItem;
import co.simplon.flashback.services.RetrospectiveService;
import co.simplon.flashback.services.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final RetrospectiveService retroService;

    public AdminController(UserService userService,
	    RetrospectiveService retroService) {
	this.userService = userService;
	this.retroService = retroService;
    }

    @GetMapping("/users")
    public Collection<UserItem> getAllUserItems() {
	return userService.getAllUserItems();
    }

    @DeleteMapping("/{id}/remove-user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
	    @PathVariable("id") Long userId) {
	userService.deleteUser(userId);
    }

    @GetMapping("/retrospectives")
    public Collection<AdminRetroItem> getAllRetrospectives() {
	return retroService.getAllRetrospectives();
    }

    @DeleteMapping("/{id}/remove-retro")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRetrospectiveByAdmin(
	    @PathVariable("id") Long retroId) {
	retroService.deleteRetrospectiveByAdmin(retroId);
    }

    @GetMapping("/{id}/retro-details")
    public AdminRetroDetailsWithMovies getAdminRetroDetails(
	    @PathVariable("id") Long retrospectiveId) {
	return retroService
		.getAdminRetroDetails(retrospectiveId);
    }
}
