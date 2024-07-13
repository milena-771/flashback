package co.simplon.flashback.errors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class ValidationErrors {

    private final List<CustomFieldError> fieldErrors = new ArrayList<CustomFieldError>();

    private final List<CustomGlobalError> globalErrors = new ArrayList<CustomGlobalError>();

    ValidationErrors() {
    }

    public Object addFieldError(CustomFieldError error) {
	return fieldErrors.add(error);
    }

    public Object addGlobalError(CustomGlobalError error) {
	return globalErrors.add(error);
    }

    public List<CustomFieldError> getFieldErrors() {
	return Collections.unmodifiableList(fieldErrors);
    }

    public List<CustomGlobalError> getGlobalErrors() {
	return Collections.unmodifiableList(globalErrors);
    }

    @Override
    public String toString() {
	return " {fieldErrors=" + fieldErrors
		+ ", globalErrors=" + globalErrors + "}";
    }
}
