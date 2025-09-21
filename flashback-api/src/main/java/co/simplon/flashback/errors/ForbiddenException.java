package co.simplon.flashback.errors;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class ForbiddenException extends ApiException {

	public ForbiddenException(String code, String message) {
		super(code, message, HttpStatus.FORBIDDEN);
	}
}
