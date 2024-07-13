package co.simplon.flashback.dtos;

import java.time.LocalDate;
import java.util.Set;

import co.simplon.flashback.validation.UniqueRetroNameUpdate;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@UniqueRetroNameUpdate
public record RetrospectiveUpdate(
	@NotNull @Positive Long id,
	@NotBlank @Size(min = 1, max = 150) @Pattern(regexp = "^[a-zA-Z0-9-éàèêôûîç'’?!.,:() ]+$") String retrospectiveName,
	@NotNull @FutureOrPresent LocalDate startDate,
	@NotNull @FutureOrPresent LocalDate endDate,
	@NotBlank @Size(min = 1, max = 1000) @Pattern(regexp = "^[a-zA-Z0-9-éàèâêôûùîç'’\\\\\\\",.:()?!$€% ]+$") String description,
	@NotNull @Positive Long deviceId,
	@NotNull Set<Long> movieId

) {

}
