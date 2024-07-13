package co.simplon.flashback.dtos;

import java.util.Set;

public class MovieForSearch {

    private Long id;

    private String title;

    private String poster;

    private int releaseYear;

    private String summary;

    private Set<DirectorDetails> directors;

    public MovieForSearch() {
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getPoster() {
	return poster;
    }

    public void setPoster(String poster) {
	this.poster = poster;
    }

    public int getReleaseYear() {
	return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
	this.releaseYear = releaseYear;
    }

    public Set<DirectorDetails> getDirectors() {
	return directors;
    }

    public void setDirectors(
	    Set<DirectorDetails> directors) {
	this.directors = directors;
    }

    public String getSummary() {
	return summary;
    }

    public void setSummary(String summary) {
	this.summary = summary;
    }

    @Override
    public String toString() {
	return " {id=" + id + ", title=" + title
		+ ", poster=" + poster + ", releaseYear="
		+ releaseYear + ", summary=" + summary
		+ ", directors=" + directors + "}";
    }
}
