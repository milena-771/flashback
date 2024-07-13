package co.simplon.flashback.dtos;

public interface MovieDetails {

    Long getId();

    String getIsan();

    String getTitle();

    int getReleaseYear();

    String getPoster();

    String getTrailer();

    String getSummary();

    GenreDetails getGenre();

}
