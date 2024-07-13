package co.simplon.flashback.dtos;

import java.util.Set;

public class ParticipantRetroForUpdate {

    private ParticipantRetroDetails retroDetails;

    private Set<MovieForSearch> movieDetails;

    public ParticipantRetroForUpdate() {
    }

    public ParticipantRetroDetails getRetroDetails() {
	return retroDetails;
    }

    public void setRetroDetails(
	    ParticipantRetroDetails retroDetails) {
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
