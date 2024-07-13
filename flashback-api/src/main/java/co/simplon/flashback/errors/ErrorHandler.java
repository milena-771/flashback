package co.simplon.flashback.errors;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ErrorHandler
	extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    public ErrorHandler(MessageSource messageSource) {
	this.messageSource = messageSource;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
	    MethodArgumentNotValidException ex,
	    HttpHeaders headers, HttpStatusCode status,
	    WebRequest request) {
	return handleBindException(ex, headers, status,
		request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(
	    BindException ex, HttpHeaders headers,
	    HttpStatusCode status, WebRequest request) {
	List<FieldError> fieldErrors = ex.getFieldErrors();
	List<ObjectError> globalErrors = ex
		.getGlobalErrors();
	ValidationErrors errors = new ValidationErrors();
	fieldErrors.forEach(e -> errors.addFieldError(
		new CustomFieldError(e.getField(),
			e.getCode())));
	globalErrors.forEach(e -> errors.addGlobalError(
		new CustomGlobalError(e.getCode(),
			e.getDefaultMessage())));
	return handleExceptionInternal(ex, errors, headers,
		status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
	    Exception ex, Object body, HttpHeaders headers,
	    HttpStatusCode status, WebRequest request) {
	return super.handleExceptionInternal(ex, body,
		headers, status, request);
    }

    @ExceptionHandler(value = FlashbackException.class)
    protected ResponseEntity<Object> handleFlashbackException(
	    FlashbackException ex, WebRequest request) {
	String code = ex.getPrefix() + ex.getCode();
	String errorMessage = ex.getMessage();
	CustomGlobalError globalError = new CustomGlobalError(
		code, errorMessage);
	return handleExceptionInternal(ex, globalError,
		new HttpHeaders(),
		HttpStatusCode.valueOf(HttpStatus
			.valueOf(ex.getCode()).value()),
		request);
    }
}
