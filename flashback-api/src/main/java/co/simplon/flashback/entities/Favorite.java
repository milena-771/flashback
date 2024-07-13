package co.simplon.flashback.entities;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_favorites")
public class Favorite extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Favorite() {
	// Required no-arg constructor
    }

    public Movie getMovie() {
	return movie;
    }

    public void setMovie(Movie movie) {
	this.movie = movie;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    @Override
    public int hashCode() {
	return Objects.hash(movie, user);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj instanceof Favorite other) {
	    return Objects.equals(movie, other.movie)
		    && Objects.equals(user, other.user);
	}
	return false;
    }

    @Override
    public String toString() {
	return " {movie=" + movie + ", user=" + user + "}";
    }
}
