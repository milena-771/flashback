package co.simplon.flashback.dtos;

import java.util.Set;

public class AdminRetroDetailsWithMovies {

    private AdminRetroDetails retroDetails;

    private Set<MovieForSearch> movieDetails;

    public AdminRetroDetailsWithMovies() {
    }

    public AdminRetroDetails getRetroDetails() {
	return retroDetails;
    }

    public void setRetroDetails(
	    AdminRetroDetails retroDetails) {
	this.retroDetails = retroDetails;
    }

    public Set<MovieForSearch> getMovieDetails() {
	return movieDetails;
    }

    public void setMovieDetails(
	    Set<MovieForSearch> movieDetails) {
	this.movieDetails = movieDetails;
    }

    @Override
    public String toString() {
	return " {retroDetails=" + retroDetails
		+ ", movieDetails=" + movieDetails + "}";
    }
}
