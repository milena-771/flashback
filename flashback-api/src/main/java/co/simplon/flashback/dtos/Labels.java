package co.simplon.flashback.dtos;

import java.util.Collection;

public class Labels {

    private Collection<DirectorDetails> allDirectors;

    private Collection<GenreDetails> allGenres;

    public Labels() {
    }

    public Collection<DirectorDetails> getAllDirectors() {
	return allDirectors;
    }

    public void setAllDirectors(
	    Collection<DirectorDetails> allDirectors) {
	this.allDirectors = allDirectors;
    }

    public Collection<GenreDetails> getAllGenres() {
	return allGenres;
    }

    public void setAllGenres(
	    Collection<GenreDetails> allGenres) {
	this.allGenres = allGenres;
    }

    @Override
    public String toString() {
	return " {allDirectors=" + allDirectors
		+ ", allGenres=" + allGenres + "}";
    }
}
