package co.simplon.flashback.dtos;

import java.time.LocalDate;

public interface UserItem {

    Long getId();

    String getFirstname();

    String getLastname();

    String getEmail();

    LocalDate getCreatedAt();
}
