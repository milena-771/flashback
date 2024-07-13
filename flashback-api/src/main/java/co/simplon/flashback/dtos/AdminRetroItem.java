package co.simplon.flashback.dtos;

import java.time.LocalDate;

public interface AdminRetroItem {

    Long getId();

    String getRetrospectiveName();

    LocalDate getStartDate();

    LocalDate getEndDate();

    String getOrganizerEmail();
}
