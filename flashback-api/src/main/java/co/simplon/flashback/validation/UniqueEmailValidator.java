package co.simplon.flashback.validation;

import co.simplon.flashback.services.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements
	ConstraintValidator<UniqueEmail, String> {

    private UserService service;

    public UniqueEmailValidator(UserService service) {
	this.service = service;
    }

    @Override
    public boolean isValid(String value,
	    ConstraintValidatorContext context) {

	if (value == null) {
	    return true;
	}
	if (service.existsByEmail(value)) {
	    return false;
	}
	return true;

    }

}
