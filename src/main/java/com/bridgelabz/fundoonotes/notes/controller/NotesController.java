package com.bridgelabz.fundoonotes.notes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.notes.exceptions.CreateDtoException;
import com.bridgelabz.fundoonotes.notes.exceptions.EditDtoException;
import com.bridgelabz.fundoonotes.notes.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonotes.notes.model.CreateDTO;
import com.bridgelabz.fundoonotes.notes.model.EditNoteDto;
import com.bridgelabz.fundoonotes.notes.model.Note;
import com.bridgelabz.fundoonotes.notes.model.NoteResponseDto;
import com.bridgelabz.fundoonotes.notes.model.ViewNoteDto;
import com.bridgelabz.fundoonotes.notes.services.NotesService;
import com.bridgelabz.fundoonotes.user.exception.TokenParsingException;

@RestController
public class NotesController {

	@Autowired
	NotesService notesService;
	
	//------------------Create Note---------------------------
	
	@RequestMapping(value="/create",method= RequestMethod.POST)
	public ResponseEntity<ViewNoteDto > createNote(@RequestBody CreateDTO createDto,@RequestParam(value = "token") String token) throws CreateDtoException, TokenParsingException, NoteNotFoundException{
		
		
		ViewNoteDto view = notesService.create(createDto,token);
	
		return new ResponseEntity<>(view,HttpStatus.OK);
		
	}
	
	//-------------------Open Notes---------------------------
	@RequestMapping(value="/open",method = RequestMethod.POST)
	public ResponseEntity<List<ViewNoteDto>> openNotes(@RequestParam(value="token") String token) throws TokenParsingException, NoteNotFoundException{
		
		List<ViewNoteDto> notes = notesService.openAllNotes(token);
		 
		return new ResponseEntity<List<ViewNoteDto>>(notes,HttpStatus.OK);
	}
	
	//------------------Edit Notes-----------------------------
	@RequestMapping(value="/edit",method = RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> editNotes(@RequestParam(value="token") String token,@RequestParam(value="noteId") String noteId ,@RequestBody EditNoteDto editNoteDto) throws TokenParsingException, EditDtoException, NoteNotFoundException {
		
		notesService.editNote(token,editNoteDto,noteId);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Notes Updated");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	//------------------Delete Notes---------------------------
	
	@RequestMapping(value="/delete",method = RequestMethod.POST)
	public ResponseEntity<NoteResponseDto> deleteNote(@RequestParam(value="token") String token,@RequestParam(value="noteId") String noteId) throws TokenParsingException, NoteNotFoundException{
		
		notesService.delete(token,noteId);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Note Deleted SuccessFully");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
}
