package co.simplon.flashback.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PosterTypeValidator implements
	ConstraintValidator<PosterType, MultipartFile> {

    private static final List<String> VALID_POSTER_TYPES = new ArrayList<String>();
    static {
	VALID_POSTER_TYPES.add("image/png");
	VALID_POSTER_TYPES.add("image/jpeg");
    }

    @Override
    public boolean isValid(MultipartFile value,
	    ConstraintValidatorContext context) {

	if (value == null) {
	    return true;
	}

	if (VALID_POSTER_TYPES
		.contains(value.getContentType())) {
	    return true;
	}
	return false;
    }

}
