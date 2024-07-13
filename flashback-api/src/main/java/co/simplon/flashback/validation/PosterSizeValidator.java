package co.simplon.flashback.validation;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PosterSizeValidator implements
	ConstraintValidator<PosterSize, MultipartFile> {

    private static final long KB = 1000;

    private long maxSizeInKB;

    @Override
    public void initialize(PosterSize posterSize) {
	this.maxSizeInKB = posterSize.maxSizeInKB();
    }

    @Override
    public boolean isValid(MultipartFile value,
	    ConstraintValidatorContext context) {

	if (value == null) {
	    return true;
	}
	return value.getSize() < (maxSizeInKB * KB);
    }

}
