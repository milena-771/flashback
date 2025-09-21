package co.simplon.flashback.errors;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class UnauthorizedException extends ApiException {

	public UnauthorizedException(String code, String message) {
		super(code, message, HttpStatus.UNAUTHORIZED);
	}
}
