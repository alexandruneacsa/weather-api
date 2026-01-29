package com.weather.demo.aop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalAdviceController {
	
	@ExceptionHandler({Exception.class})
	public ProblemDetail handleGlobalException(final Exception ex) {
		
		log.error("Unhandled exception", ex);
		String detail = ex.getMessage() != null ? ex.getMessage() : HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
		return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, detail);
	}
	
	@ExceptionHandler({IllegalArgumentException.class})
	public ProblemDetail handleBadRequest(final IllegalArgumentException ex) {
		
		log.warn("Bad request: {}", ex.getMessage());
		String detail = ex.getMessage() != null ? ex.getMessage() : HttpStatus.BAD_REQUEST.getReasonPhrase();
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
	}
	
	@ExceptionHandler({ResourceNotFoundException.class})
	public ProblemDetail handleNotFound(final ResourceNotFoundException ex) {
		
		String detail = ex.getMessage() != null ? ex.getMessage() : HttpStatus.NOT_FOUND.getReasonPhrase();
		return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, detail);
	}
}
