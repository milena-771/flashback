package co.simplon.flashback.validation;

import co.simplon.flashback.services.RetrospectiveService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueRetroNameValidator implements
	ConstraintValidator<UniqueRetroName, String> {

    private RetrospectiveService service;

    public UniqueRetroNameValidator(
	    RetrospectiveService service) {
	this.service = service;
    }

    @Override
    public boolean isValid(String value,
	    ConstraintValidatorContext context) {

	if (value == null) {
	    return true;
	}
	if (service.existsByRetroName(value)) {
	    return false;
	}
	return true;
    }

}
