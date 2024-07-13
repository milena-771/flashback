package co.simplon.flashback.entities;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_direction")
public class Direction extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;

    public Direction() {
	// Required no-arg constructor
    }

    public Movie getMovie() {
	return movie;
    }

    public void setMovie(Movie movie) {
	this.movie = movie;
    }

    public Director getDirector() {
	return director;
    }

    public void setDirector(Director director) {
	this.director = director;
    }

    @Override
    public int hashCode() {
	return Objects.hash(director, movie);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj instanceof Direction other) {
	    return Objects.equals(director, other.director)
		    && Objects.equals(movie, other.movie);
	}
	return false;
    }

    @Override
    public String toString() {
	return " {movie=" + movie + ", director=" + director
		+ "}";
    }

}
