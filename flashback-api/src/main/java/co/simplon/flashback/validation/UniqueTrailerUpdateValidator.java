package co.simplon.flashback.validation;

import java.util.Optional;

import co.simplon.flashback.dtos.MovieUpdate;
import co.simplon.flashback.services.MovieService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueTrailerUpdateValidator implements
	ConstraintValidator<UniqueTrailerUpdate, MovieUpdate> {

    private MovieService service;

    public UniqueTrailerUpdateValidator(
	    MovieService service) {
	this.service = service;
    }

    @Override
    public boolean isValid(MovieUpdate inputs,
	    ConstraintValidatorContext context) {

	if (inputs.trailer() == null) {
	    return true;
	}

	String trailer = inputs.trailer();

	if (!trailer.isEmpty()) {
	    Optional<Long> id = service
		    .existsByTrailerForUpdate(inputs);
	    if (id.isPresent()) {
		return false;
	    }
	}
	return true;
    }

}
