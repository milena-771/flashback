package co.simplon.flashback.dtos;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import co.simplon.flashback.validation.PosterSize;
import co.simplon.flashback.validation.PosterType;
import co.simplon.flashback.validation.UniqueTrailerUpdate;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@UniqueTrailerUpdate
public record MovieUpdate(

	@NotNull @Positive Long id,

	@NotBlank @Size(max = 100) @Pattern(regexp = "^[a-zA-Z0-9-éàèêôûîç:,.'’ ]+$") String title,

	@NotNull @Positive @Min(value = 1895) @Max(value = 2025) int releaseYear,

	@PosterType @PosterSize(maxSizeInKB = 200) MultipartFile poster,

	@Size(max = 300) @Pattern(regexp = "^[a-zA-Z0-9-&=?/:_.]*$") String trailer,

	@NotBlank @Size(max = 1000) @Pattern(regexp = "^[a-zA-Z0-9-éàèâêôûùîç'’\",.:()?!$€% ]+$") String summary,

	@NotNull @Positive Long genreId,

	@NotNull @NotEmpty List<Long> directorId) {

}
