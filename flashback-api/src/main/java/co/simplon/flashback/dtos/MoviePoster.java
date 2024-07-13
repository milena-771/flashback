package co.simplon.flashback.dtos;

public class MoviePoster {

    private String poster;

    public MoviePoster() {
    }

    public String getPoster() {
	return poster;
    }

    public void setPoster(String poster) {
	this.poster = poster;
    }

    @Override
    public String toString() {
	return " {poster=" + poster + "}";
    }

}
