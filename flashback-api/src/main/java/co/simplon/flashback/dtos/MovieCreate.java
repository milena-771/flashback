package co.simplon.flashback.dtos;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import co.simplon.flashback.validation.PosterSize;
import co.simplon.flashback.validation.PosterType;
import co.simplon.flashback.validation.UniqueIsan;
import co.simplon.flashback.validation.UniqueTrailer;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MovieCreate(
	@UniqueIsan @NotBlank @Size(max = 33) @Pattern(regexp = "^0000-000[A-Z0-9-]{25}+$") String isan,

	@NotBlank @Size(max = 100) @Pattern(regexp = "^[a-zA-Z0-9-éàâèêôûîç:,.'’ ]+$") String title,

	@NotNull @Positive @Min(value = 1895) @Max(value = 2025) int releaseYear,

	@NotNull @PosterType @PosterSize(maxSizeInKB = 200) MultipartFile poster,

	@UniqueTrailer @Size(max = 300) @Pattern(regexp = "^[a-zA-Z0-9-&=?/:_.]*$") String trailer,

	@NotBlank @Size(max = 1000) @Pattern(regexp = "^[a-zA-Z0-9-éàèâêôûùîç'’\",.:()?!$€% ]+$") String summary,

	@NotNull @Positive Long genreId,

	@NotNull @NotEmpty Set<Long> directorId) {

}
