package co.simplon.flashback.controllers;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.simplon.flashback.dtos.Labels;
import co.simplon.flashback.dtos.MovieCreate;
import co.simplon.flashback.dtos.MovieForSearch;
import co.simplon.flashback.dtos.MovieForUpdate;
import co.simplon.flashback.dtos.MovieItem;
import co.simplon.flashback.dtos.MoviePoster;
import co.simplon.flashback.dtos.MovieUpdate;
import co.simplon.flashback.dtos.MoviesForSearchAndFavorites;
import co.simplon.flashback.services.MovieService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/movies")
public class MovieController {

	private final MovieService service;

	public MovieController(MovieService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void post(@ModelAttribute @Valid MovieCreate inputs) {
		service.create(inputs);
	}

	@GetMapping("/for-edit")
	public Collection<MovieItem> getAllForEdit() {
		return service.getAllForEdit();
	}

	@GetMapping("/for-search")
	public MoviesForSearchAndFavorites getAllForSearch() {
		return service.getAllForSearch();
	}

	@GetMapping("/labels")
	public Labels getAllLabels() {
		return service.getAllLabels();
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		service.delete(id);
	}

	@GetMapping("/{id}/for-update")
	public MovieForUpdate forUpdate(@PathVariable("id") Long id) {
		return service.forUpdate(id);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public MoviePoster update(@PathVariable("id") Long id,
			@ModelAttribute @Valid MovieUpdate inputs) {
		return service.update(id, inputs);
	}

	@GetMapping("/by-title")
	public Collection<MovieForSearch> searchByTitle(
			@RequestParam String title) {
		return service.searchByTitle(title);
	}

	@GetMapping("/by-director")
	public Collection<MovieForSearch> searchByDirectorLastname(
			@RequestParam String lastname) {
		return service.searchByDirectorLastname(lastname);
	}

	@GetMapping("/by-genre")
	public Collection<MovieForSearch> searchByGenre(
			@RequestParam String genre) {
		return service.searchByGenre(genre);
	}

}
