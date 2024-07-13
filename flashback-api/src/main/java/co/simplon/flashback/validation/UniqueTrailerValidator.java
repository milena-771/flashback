package co.simplon.flashback.validation;

import co.simplon.flashback.services.MovieService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueTrailerValidator implements
	ConstraintValidator<UniqueTrailer, String> {

    private MovieService service;

    public UniqueTrailerValidator(MovieService service) {
	this.service = service;
    }

    @Override
    public boolean isValid(String value,
	    ConstraintValidatorContext context) {
	if (value == null) {
	    return true;
	}
	if ((!value.isEmpty())
		&& service.existsByTrailer(value)) {
	    return false;
	}
	return true;
    }

}
