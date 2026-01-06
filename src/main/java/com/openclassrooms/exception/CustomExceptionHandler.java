package com.openclassrooms.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;

@RestControllerAdvice
public class CustomExceptionHandler {
	
		@ExceptionHandler(Exception.class)
		public ProblemDetail handleAuthenticationException(Exception ex) {
		ProblemDetail errorDetail = null;
			if(ex instanceof BadCredentialsException) {
				errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), ex.getMessage());
				errorDetail.setProperty("Access_Denied_reason","Authentication Failure");
			}
			if(ex instanceof ExpiredJwtException) {
				errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), ex.getMessage());
				errorDetail.setProperty("Access_Denied_reason","Jwt token already expired");
			}
			return errorDetail;
		
	}

}
