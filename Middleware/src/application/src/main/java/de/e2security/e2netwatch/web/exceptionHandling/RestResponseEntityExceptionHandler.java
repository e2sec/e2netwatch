package de.e2security.e2netwatch.web.exceptionHandling;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import de.e2security.e2netwatch.utils.constants.MyCustomError;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(value = { IllegalArgumentException.class })
	protected ResponseEntity<Object> handleIllegalArgument(RuntimeException ex, WebRequest request) {
		    	
    	MyCustomError error = new MyCustomError(
    			HttpStatus.BAD_REQUEST.value(), 
    			HttpStatus.BAD_REQUEST.getReasonPhrase(), 
    			ex.getMessage());
    	
    	return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler(value = { NullPointerException.class })
	protected ResponseEntity<Object> handleNullPointer(RuntimeException ex, WebRequest request) {
		    	
    	MyCustomError error = new MyCustomError(
    			HttpStatus.BAD_REQUEST.value(), 
    			HttpStatus.BAD_REQUEST.getReasonPhrase(), 
    			ex.getMessage());
    	
    	return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler(value = { EntityExistsException.class })
	protected ResponseEntity<Object> handleEntityExists(RuntimeException ex, WebRequest request) {
		    	
    	MyCustomError error = new MyCustomError(
    			HttpStatus.CONFLICT.value(), 
    			HttpStatus.CONFLICT.getReasonPhrase(), 
    			ex.getMessage());
    	
    	return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.CONFLICT, request);
	}
	
	@ExceptionHandler(value = { EntityNotFoundException.class })
	protected ResponseEntity<Object> handleEntityNotFound(RuntimeException ex, WebRequest request) {
		    	
    	MyCustomError error = new MyCustomError(
    			HttpStatus.BAD_REQUEST.value(), 
    			HttpStatus.BAD_REQUEST.getReasonPhrase(), 
    			ex.getMessage());
    	
    	return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

}
