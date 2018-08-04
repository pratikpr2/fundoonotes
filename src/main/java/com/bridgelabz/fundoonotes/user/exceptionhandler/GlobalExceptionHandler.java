package com.bridgelabz.fundoonotes.user.exceptionhandler;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.fundoonotes.user.exception.ActivationException;
import com.bridgelabz.fundoonotes.user.exception.ChangePassException;
import com.bridgelabz.fundoonotes.user.exception.MailException;
import com.bridgelabz.fundoonotes.user.exception.MalformedUUIDException;
import com.bridgelabz.fundoonotes.user.exception.RegistrationException;
import com.bridgelabz.fundoonotes.user.exception.TokenParsingException;
import com.bridgelabz.fundoonotes.user.model.ResponseDto;

@ControllerAdvice
public class GlobalExceptionHandler{

	
	private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(RegistrationException.class)
	public ResponseEntity<ResponseDto> handleRegistartionException(RegistrationException exception){
		logger.error("Error Occured While Registration"+ exception.getMessage(),exception);
	
		ResponseDto response = new ResponseDto();
		
		response.setMessage(exception.getMessage());
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(LoginException.class)
	public ResponseEntity<ResponseDto> handleLoginException(LoginException exception){
		logger.error("Error Ocuured While Login"+ exception.getMessage(),exception);
		
		ResponseDto response = new ResponseDto();
		
		response.setMessage(exception.getMessage());
		response.setStatus(0);
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseDto> handleOtherException(Exception exception){
		logger.error("Error Occured: "+exception.getMessage(),exception);
		
		ResponseDto response = new ResponseDto();
		
		response.setMessage(exception.getMessage());
		response.setStatus(-1);
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(ChangePassException.class)
	public ResponseEntity<ResponseDto> handleChangePassException(ChangePassException exception){
		logger.error("Error Occured: "+exception.getMessage(),exception);
		
		ResponseDto response = new ResponseDto();
		
		response.setMessage(exception.getMessage());
		response.setStatus(2);
		
		return new ResponseEntity<>(response,HttpStatus.CONFLICT);
		
	}
	@ExceptionHandler(ActivationException.class)
	public ResponseEntity<ResponseDto> handleActivationException(ActivationException exception){
		logger.error("Error Occured: "+exception.getMessage(),exception);
		
		ResponseDto response = new ResponseDto();
		
		response.setMessage(exception.getMessage());
		response.setStatus(-2);
		
		return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(MailException.class)
	public ResponseEntity<ResponseDto> handleMailException(ActivationException exception){
		logger.error("Error Occured: "+exception.getMessage(),exception);
		
		ResponseDto response = new ResponseDto();
		
		response.setMessage(exception.getMessage());
		response.setStatus(-2);
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(TokenParsingException.class)
	public ResponseEntity<ResponseDto> tokenParsingException(TokenParsingException exception){
		
		logger.error("Error Occured: " + exception.getMessage(),exception);
		
		ResponseDto response = new ResponseDto();
		
		response.setMessage(exception.getMessage());
		response.setStatus(-1);
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(MalformedUUIDException.class)
	public ResponseEntity<ResponseDto> malformedUUIDException(MalformedUUIDException exception){
		
		logger.error("Error Occured while parsing UUID: " + exception.getMessage(),exception);
		
		ResponseDto response = new ResponseDto();
		
		response.setMessage(exception.getMessage());
		response.setStatus(-5);
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
}
