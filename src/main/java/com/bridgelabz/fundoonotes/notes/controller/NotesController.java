package com.bridgelabz.fundoonotes.notes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.notes.exceptions.CreateDtoException;
import com.bridgelabz.fundoonotes.notes.model.CreateDTO;
import com.bridgelabz.fundoonotes.notes.model.NoteResponseDto;
import com.bridgelabz.fundoonotes.notes.services.NotesService;
import com.bridgelabz.fundoonotes.user.exception.TokenParsingException;
import com.bridgelabz.fundoonotes.user.token.JwtToken;

@RestController
public class NotesController {

	@Autowired
	NotesService notesService;
	
	@Autowired
	JwtToken jwt;
	
	@RequestMapping(value="/create",method= RequestMethod.POST)
	public ResponseEntity<NoteResponseDto> createNote(@RequestBody CreateDTO createDto,@RequestParam(value = "token") String token) throws CreateDtoException, TokenParsingException{
		
		jwt.parseJWT(token);
		
		notesService.create(createDto);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("New Note Created");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
}
