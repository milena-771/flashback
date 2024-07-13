package co.simplon.flashback.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FavoriteAdd(@NotNull @Positive Long movieId) {

}
