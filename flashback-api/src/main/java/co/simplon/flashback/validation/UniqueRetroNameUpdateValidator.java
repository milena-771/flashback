package co.simplon.flashback.validation;

import java.util.Optional;

import co.simplon.flashback.dtos.RetrospectiveUpdate;
import co.simplon.flashback.services.RetrospectiveService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueRetroNameUpdateValidator implements
	ConstraintValidator<UniqueRetroNameUpdate, RetrospectiveUpdate> {

    private RetrospectiveService service;

    public UniqueRetroNameUpdateValidator(
	    RetrospectiveService service) {
	this.service = service;
    }

    @Override
    public boolean isValid(RetrospectiveUpdate inputs,
	    ConstraintValidatorContext context) {

	if (inputs.retrospectiveName() == null) {
	    return true;
	}

	String retrospectiveName = inputs
		.retrospectiveName();

	if (!retrospectiveName.isEmpty()) {
	    Optional<Long> id = service
		    .existsByRetrospectiveNameForUpdate(
			    inputs);
	    if (id.isPresent()) {
		/*
		 * context.buildConstraintViolationWithTemplate(
		 * "This retrospective name already exists") .addPropertyNode(
		 * "retrospectiveName") .addConstraintViolation();
		 */
		return false;
	    }
	}
	return true;
    }

}
