package co.simplon.flashback.validation;

import co.simplon.flashback.services.MovieService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueIsanValidator
	implements ConstraintValidator<UniqueIsan, String> {

    private MovieService service;

    public UniqueIsanValidator(MovieService service) {
	this.service = service;
    }

    @Override
    public boolean isValid(String value,
	    ConstraintValidatorContext context) {

	if (value == null) {
	    return true;
	}
	if (service.existsByIsan(value)) {
	    return false;
	}
	return true;
    }

}
