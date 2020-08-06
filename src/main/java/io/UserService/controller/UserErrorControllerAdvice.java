package io.UserService.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import io.UserService.exceptionhandler.DataNotFoundException;
import io.UserService.exceptionhandler.DuplicateDataNotAllowedException;
import io.UserService.exceptionhandler.NullFormatException;
import io.UserService.model.ErrorModel;

@ControllerAdvice
public class UserErrorControllerAdvice extends ResponseEntityExceptionHandler{
	
	/*
		Responsibility: Method to map DataNotFoundException with proper Response Entity of Error Message.
		Parameter: Exception Class (DataNotFoundException)
		Return Type: ResponseEntity
	*/
	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<ErrorModel> mapException(DataNotFoundException e) {
		ErrorModel errorObj=new ErrorModel(HttpStatus.NOT_FOUND.value(),e.getMessage());
		return new ResponseEntity<>(errorObj,HttpStatus.NOT_FOUND);
	}

	/*
		Responsibility: Method to map DuplicateDataNotAllowedException with proper Response Entity of Error Message.
		Parameter: Exception Class (DuplicateDataNotAllowedException)
		Return Type: ResponseEntity
	*/
	@ExceptionHandler(DuplicateDataNotAllowedException.class)
	public ResponseEntity<ErrorModel> mapException(DuplicateDataNotAllowedException e) {
		ErrorModel errorObj=new ErrorModel(HttpStatus.CONFLICT.value(),e.getMessage());
		return new ResponseEntity<>(errorObj,HttpStatus.CONFLICT);
	}

	//Overriding existing handleHttpRequestMethodNotSupported
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorModel errorObj=new ErrorModel(HttpStatus.METHOD_NOT_ALLOWED.value(),ex.getMessage());
		return new ResponseEntity<>(errorObj,HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	/*
		Responsibility: Method to map NullFormatException with proper Response Entity of Error Message.
		Parameter: Exception Class (NullFormatException)
		Return Type: ResponseEntity
	*/
	@ExceptionHandler(NullFormatException.class)
	public ResponseEntity<ErrorModel> mapException(NullFormatException e) {
		ErrorModel errorObj=new ErrorModel(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
		return new ResponseEntity<>(errorObj,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	//Overriding existing handleHttpMessageNotReadable
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorModel errorObj=new ErrorModel(HttpStatus.BAD_REQUEST.value(),ex.getMessage());
		return new ResponseEntity<>(errorObj,HttpStatus.BAD_REQUEST);
	}
	
}
