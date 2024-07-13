package co.simplon.flashback.entities;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_program")
public class Program extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "retrospective_id")
    private Retrospective retrospective;

    public Program() {
	// Required no-arg constructor
    }

    public Movie getMovie() {
	return movie;
    }

    public void setMovie(Movie movie) {
	this.movie = movie;
    }

    public Retrospective getRetrospective() {
	return retrospective;
    }

    public void setRetrospective(
	    Retrospective retrospective) {
	this.retrospective = retrospective;
    }

    @Override
    public int hashCode() {
	return Objects.hash(movie, retrospective);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj instanceof Program other) {
	    return Objects.equals(movie, other.movie)
		    && Objects.equals(retrospective,
			    other.retrospective);
	}
	return false;
    }

    @Override
    public String toString() {
	return " {movie=" + movie + ", retrospective="
		+ retrospective + "}";
    }
}
