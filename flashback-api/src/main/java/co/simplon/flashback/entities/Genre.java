package co.simplon.flashback.entities;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_genres")
public class Genre extends AbstractEntity {

    @Column(name = "genre_name")
    private String genreName;

    public Genre() {
	// Required no-arg constructor
    }

    public String getGenreName() {
	return genreName;
    }

    public void setGenreName(String genreName) {
	this.genreName = genreName;
    }

    @Override
    public int hashCode() {
	return Objects.hash(genreName);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj instanceof Genre other) {
	    Objects.equals(genreName, other.genreName);
	}
	return false;
    }

    @Override
    public String toString() {
	return " {genreName=" + genreName + "}";
    }

}