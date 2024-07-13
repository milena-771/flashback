package co.simplon.flashback.dtos;

import java.time.LocalDate;

public interface RetroItem {

    Long getId();

    String getRetrospectiveName();

    LocalDate getStartDate();

    LocalDate getEndDate();

    int getMoviesNumber();

}
