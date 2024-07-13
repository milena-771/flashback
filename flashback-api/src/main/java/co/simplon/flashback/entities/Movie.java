package co.simplon.flashback.entities;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_movies")
public class Movie extends AbstractEntity {

    @Column(name = "isan")
    private String isan;

    @Column(name = "title")
    private String title;

    @Column(name = "release_year")
    private int releaseYear;

    @Column(name = "poster")
    private String poster;

    @Column(name = "trailer")
    private String trailer;

    @Column(name = "summary")
    private String summary;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public Movie() {
	// Required no-arg constructor
    }

    public String getIsan() {
	return isan;
    }

    public void setIsan(String isan) {
	this.isan = isan;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public int getReleaseYear() {
	return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
	this.releaseYear = releaseYear;
    }

    public String getPoster() {
	return poster;
    }

    public void setPoster(String poster) {
	this.poster = poster;
    }

    public String getTrailer() {
	return trailer;
    }

    public void setTrailer(String trailer) {
	this.trailer = trailer;
    }

    public String getSummary() {
	return summary;
    }

    public void setSummary(String summary) {
	this.summary = summary;
    }

    public Genre getGenre() {
	return genre;
    }

    public void setGenre(Genre genre) {
	this.genre = genre;
    }

    @Override
    public int hashCode() {
	return Objects.hash(isan);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj instanceof Movie other) {
	    return Objects.equals(isan, other.isan);
	}
	return false;
    }

    @Override
    public String toString() {
	return " {isan=" + isan + ", title=" + title
		+ ", releaseYear=" + releaseYear
		+ ", poster=" + poster + ", trailer="
		+ trailer + ", summary=" + summary
		+ ", genre=" + genre + "}";
    }

}
