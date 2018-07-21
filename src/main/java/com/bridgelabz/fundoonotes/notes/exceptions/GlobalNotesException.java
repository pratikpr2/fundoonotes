package com.bridgelabz.fundoonotes.notes.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.fundoonotes.notes.model.NoteResponseDto;

@ControllerAdvice
public class GlobalNotesException {

	private final Logger logger = LoggerFactory.getLogger(GlobalNotesException.class);
	
	@ExceptionHandler(CreateDtoException.class)
	public ResponseEntity<NoteResponseDto> handleRegistartionException(CreateDtoException exception){
		logger.error("Error Occured While Creating Note: "+ exception.getMessage(),exception);
	
		NoteResponseDto response = new NoteResponseDto();
		
		response.setMessage(exception.getMessage());
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NoteNotFoundException.class)
	public ResponseEntity<NoteResponseDto> handleRegistartionException(NoteNotFoundException exception){
		logger.error("Error Occured While Finding Notes: "+ exception.getMessage(),exception);
	
		NoteResponseDto response = new NoteResponseDto();
		
		response.setMessage(exception.getMessage());
		response.setStatus(0);
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(EditDtoException.class)
	public ResponseEntity<NoteResponseDto> handleRegistartionException(EditDtoException exception){
		logger.error("Error Occured While Editing Notes: "+ exception.getMessage(),exception);
	
		NoteResponseDto response = new NoteResponseDto();
		
		response.setMessage(exception.getMessage());
		response.setStatus(-1);
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(UnauthorizedUserException.class)
	public ResponseEntity<NoteResponseDto> unauthorizedUserException(UnauthorizedUserException exception){
		logger.error("Error Occured While Authenticating User Notes: "+ exception.getMessage(),exception);
	
		NoteResponseDto response = new NoteResponseDto();
		
		response.setMessage(exception.getMessage());
		response.setStatus(2);
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(NoteNotTrashedException.class)
	public ResponseEntity<NoteResponseDto> handleRegistartionException(NoteNotTrashedException exception){
		logger.error("Error Occured While Trashing Notes: "+ exception.getMessage(),exception);
	
		NoteResponseDto response = new NoteResponseDto();
		
		response.setMessage(exception.getMessage());
		response.setStatus(-2);
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<NoteResponseDto> handleGlobalException(Exception exception){
		logger.error("Error Occured : "+ exception.getMessage(),exception);
	
		NoteResponseDto response = new NoteResponseDto();
		
		response.setMessage(exception.getMessage());
		response.setStatus(3);
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	
}
