package co.simplon.flashback.dtos;

import java.util.Collection;

public class MoviesForSearchAndFavorites {

    private Collection<MovieForSearch> movies;

    private Collection<MovieFavorite> favorites;

    public MoviesForSearchAndFavorites() {
    }

    public Collection<MovieForSearch> getMovies() {
	return movies;
    }

    public void setMovies(
	    Collection<MovieForSearch> movies) {
	this.movies = movies;
    }

    public Collection<MovieFavorite> getFavorites() {
	return favorites;
    }

    public void setFavorites(
	    Collection<MovieFavorite> favorites) {
	this.favorites = favorites;
    }

    @Override
    public String toString() {
	return " {movies=" + movies + ", favorites="
		+ favorites + "}";
    }
}
