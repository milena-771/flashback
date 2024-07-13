package co.simplon.flashback.dtos;

import java.util.Set;

public class MovieForUpdate {

    private MovieDetails movieDetails;

    private Set<DirectorDetails> directorDetails;

    public MovieForUpdate() {
    }

    public MovieDetails getMovieDetails() {
	return movieDetails;
    }

    public void setMovieDetails(MovieDetails movieDetails) {
	this.movieDetails = movieDetails;
    }

    public Set<DirectorDetails> getDirectorDetails() {
	return directorDetails;
    }

    public void setDirectorDetails(
	    Set<DirectorDetails> directorDetails) {
	this.directorDetails = directorDetails;
    }

    @Override
    public String toString() {
	return " {movieDetails=" + movieDetails
		+ ", directorDetails=" + directorDetails
		+ "}";
    }
}
