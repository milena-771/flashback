package co.simplon.flashback.dtos;

import java.util.Set;

public class OrgaRetroForUpdate {

    private RetroDetailsForUpdate retroDetails;

    private Set<MovieForSearch> movieDetails;

    public OrgaRetroForUpdate() {
    }

    public RetroDetailsForUpdate getRetroDetails() {
	return retroDetails;
    }

    public void setRetroDetails(
	    RetroDetailsForUpdate retroDetails) {
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
