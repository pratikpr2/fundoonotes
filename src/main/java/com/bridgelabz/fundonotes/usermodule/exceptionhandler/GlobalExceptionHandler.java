package com.bridgelabz.fundonotes.usermodule.exceptionhandler;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.fundonotes.usermodule.exception.ActivationException;
import com.bridgelabz.fundonotes.usermodule.exception.ChangePassException;
import com.bridgelabz.fundonotes.usermodule.exception.MailException;
import com.bridgelabz.fundonotes.usermodule.exception.RegistrationException;
import com.bridgelabz.fundonotes.usermodule.model.ResponseDto;

@ControllerAdvice
public class GlobalExceptionHandler{

	
	private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(RegistrationException.class)
	public ResponseEntity<ResponseDto> handleRegistartionException(RegistrationException exception){
		logger.info("Error Occured While Registration"+ exception.getMessage(),exception);
	
		ResponseDto response = new ResponseDto();
		
		response.setMessage(exception.getMessage());
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(LoginException.class)
	public ResponseEntity<ResponseDto> handleLoginException(LoginException exception){
		logger.info("Error Ocuured While Login"+ exception.getMessage(),exception);
		
		ResponseDto response = new ResponseDto();
		
		response.setMessage(exception.getMessage());
		response.setStatus(0);
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseDto> handleOtherException(Exception exception){
		logger.info("Error Occured: "+exception.getMessage(),exception);
		
		ResponseDto response = new ResponseDto();
		
		response.setMessage(exception.getMessage());
		response.setStatus(-1);
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(ChangePassException.class)
	public ResponseEntity<ResponseDto> handleChangePassException(ChangePassException exception){
		logger.info("Error Occured: "+exception.getMessage(),exception);
		
		ResponseDto response = new ResponseDto();
		
		response.setMessage(exception.getMessage());
		response.setStatus(2);
		
		return new ResponseEntity<>(response,HttpStatus.CONFLICT);
		
	}
	@ExceptionHandler(ActivationException.class)
	public ResponseEntity<ResponseDto> handleActivationException(ActivationException exception){
		logger.info("Error Occured: "+exception.getMessage(),exception);
		
		ResponseDto response = new ResponseDto();
		
		response.setMessage(exception.getMessage());
		response.setStatus(-2);
		
		return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(MailException.class)
	public ResponseEntity<ResponseDto> handleMailException(ActivationException exception){
		logger.info("Error Occured: "+exception.getMessage(),exception);
		
		ResponseDto response = new ResponseDto();
		
		response.setMessage(exception.getMessage());
		response.setStatus(-2);
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	
}
