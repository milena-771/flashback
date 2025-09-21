package co.simplon.flashback.errors;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class BadRequestException extends ApiException {

	public BadRequestException(String code, String message) {
		super(code, message, HttpStatus.BAD_REQUEST);
	}
}
