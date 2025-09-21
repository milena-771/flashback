package co.simplon.flashback.errors;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.persistence.QueryTimeoutException;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

	private final Logger LOG = LogManager.getLogger(ErrorHandler.class);

	// Conflits de version (optimistic locking)
	@ExceptionHandler(OptimisticLockingFailureException.class)
	public ResponseEntity<CustomGlobalError> handleOptimisticLock(
			OptimisticLockingFailureException ex) {
		LOG.error("Optimistic lock failure: {}", ex);
		CustomGlobalError error = new CustomGlobalError("OPTIMISTIC_LOCK_ERROR",
				HttpStatus.CONFLICT.value(), "Concurrent modification error");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<CustomGlobalError> handleDataIntegrityViolation(
			DataIntegrityViolationException ex) {
		LOG.error("Data integrity violation: {}", ex);
		CustomGlobalError error = new CustomGlobalError("DATA_INTEGRITY_VIOLATION",
				HttpStatus.CONFLICT.value(), "Conflict: data integrity violation");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	// suppression/lecture d’un ID inexistant
	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<CustomGlobalError> handleEmptyResult(EmptyResultDataAccessException ex) {
		LOG.warn("No result found: {}", ex.getMessage());
		CustomGlobalError error = new CustomGlobalError("NO_RESULT", HttpStatus.NOT_FOUND.value(),
				"Requested resource not found");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	// Timeout ou problème de lock
	@ExceptionHandler({ QueryTimeoutException.class, CannotAcquireLockException.class })
	public ResponseEntity<CustomGlobalError> handleDbTimeout(Exception ex) {
		LOG.error("Database timeout/lock issue: {}", ex.getMessage());
		CustomGlobalError error = new CustomGlobalError("DB_TIMEOUT",
				HttpStatus.SERVICE_UNAVAILABLE.value(), "Database timeout or lock issue");
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
	}

	// Catch-all pour les autres DataAccessException
	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<CustomGlobalError> handleDataAccess(DataAccessException ex) {
		LOG.error("Database access error: {}", ex.getMessage());
		CustomGlobalError error = new CustomGlobalError("DB_ERROR",
				HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected database error");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
			WebRequest request) {
		List<FieldError> fieldErrors = ex.getFieldErrors();
		List<ObjectError> globalErrors = ex.getGlobalErrors();
		ValidationErrors errors = new ValidationErrors();
		fieldErrors.forEach(
				e -> errors.addFieldError(new CustomFieldError(e.getField(), e.getCode())));
		globalErrors.forEach(e -> errors.addGlobalError(new CustomGlobalError(e.getCode(),
				Integer.valueOf(status.toString()), e.getDefaultMessage())));
		return handleExceptionInternal(ex, errors, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		LOG.warn("Validation error: {}", ex.getMessage());
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<CustomGlobalError> handleUnauthorized(UnauthorizedException ex) {
		LOG.warn("Unauthorized [{}]: {}", ex.getCode(), ex.getMessage());
		CustomGlobalError error = new CustomGlobalError(ex.getCode(), ex.getStatus().value(),
				ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<CustomGlobalError> handleForbidden(ForbiddenException ex) {
		LOG.error("Forbidden action [{}]: {}", ex.getCode(), ex.getMessage());
		CustomGlobalError error = new CustomGlobalError(ex.getCode(), ex.getStatus().value(),
				ex.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}

	// Erreurs métier connues
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<CustomGlobalError> handleApiException(ApiException ex) {
		LOG.error("Business exception [{}]: {}", ex.getCode(), ex.getMessage());
		CustomGlobalError error = new CustomGlobalError(ex.getCode(), ex.getStatus().value(),
				ex.getMessage());
		return ResponseEntity.status(ex.getStatus()).body(error);
	}

	@ExceptionHandler({ Exception.class, PosterUploadException.class })
	public ResponseEntity<CustomGlobalError> handleGeneric(Exception ex) {
		LOG.error("Unexpected error: {}", ex);
		CustomGlobalError error = new CustomGlobalError("INTERNAL_ERROR", 500,
				"Unexpected error occurred");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}
