package co.simplon.flashback.errors;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class PosterUploadException extends ApiException {

	public PosterUploadException(String code, String message) {
		super(code, message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
