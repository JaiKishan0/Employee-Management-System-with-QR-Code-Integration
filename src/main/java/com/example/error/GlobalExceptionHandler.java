package com.example.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(RecordNotFoundException.class)
	public ResponseEntity<?> recordNotFound(RecordNotFoundException exp)
	{
		return new ResponseEntity<>(exp.getMessage(),HttpStatus.NOT_FOUND);
	}

}
