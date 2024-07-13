package co.simplon.flashback.controllers;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.simplon.flashback.dtos.FavoriteAdd;
import co.simplon.flashback.dtos.MovieFavorite;
import co.simplon.flashback.dtos.MovieForSearch;
import co.simplon.flashback.services.FavoriteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService service;

    public FavoriteController(FavoriteService service) {
	this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void add(
	    @RequestBody @Valid FavoriteAdd inputs) {
	service.add(inputs);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long movieId) {
	service.delete(movieId);
    }

    @GetMapping
    public Collection<MovieFavorite> getAll() {
	return service.getAll();
    }

    @GetMapping("/list")
    public Collection<MovieForSearch> getAllFavoritesWithDirectors() {
	return service.getAllFavoritesWithDirectors();
    }

}
