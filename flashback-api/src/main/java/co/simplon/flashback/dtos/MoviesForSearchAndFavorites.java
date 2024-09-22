package co.simplon.flashback.dtos;

import java.util.Collection;

import org.springframework.data.domain.Page;

public class MoviesForSearchAndFavorites {

	private Page<MovieForSearch> movies;

	private Collection<MovieFavorite> favorites;

	public MoviesForSearchAndFavorites() {
	}

	public Page<MovieForSearch> getMovies() {
		return movies;
	}

	public void setMovies(Page<MovieForSearch> movies) {
		this.movies = movies;
	}

	public Collection<MovieFavorite> getFavorites() {
		return favorites;
	}

	public void setFavorites(Collection<MovieFavorite> favorites) {
		this.favorites = favorites;
	}

	@Override
	public String toString() {
		return " {movies=" + movies + ", favorites=" + favorites + "}";
	}
}
