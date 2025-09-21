package co.simplon.flashback.errors;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class NotFoundException extends ApiException {

	public NotFoundException(String code, String message) {
		super(code, message, HttpStatus.NOT_FOUND);
	}
}
