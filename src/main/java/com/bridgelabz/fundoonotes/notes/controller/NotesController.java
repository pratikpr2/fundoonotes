package com.bridgelabz.fundoonotes.notes.controller;

import java.text.ParseException;
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
import com.bridgelabz.fundoonotes.notes.exceptions.LabelException;
import com.bridgelabz.fundoonotes.notes.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonotes.notes.exceptions.NoteNotTrashedException;
import com.bridgelabz.fundoonotes.notes.exceptions.UnauthorizedUserException;
import com.bridgelabz.fundoonotes.notes.model.CreateDTO;
import com.bridgelabz.fundoonotes.notes.model.DateDto;
import com.bridgelabz.fundoonotes.notes.model.EditNoteDto;
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
	@RequestMapping(value="/openAll",method = RequestMethod.POST)
	public ResponseEntity<List<ViewNoteDto>> openNotes(@RequestParam(value="token") String token) throws TokenParsingException, NoteNotFoundException{
		
		//String userId = (String)req.getAttribute("token");

		List<ViewNoteDto> notes = notesService.openAllNotes(token);
		
		return new ResponseEntity<List<ViewNoteDto>>(notes,HttpStatus.OK);
	}
	
	//------------------Edit Notes-----------------------------
	@RequestMapping(value="/edit",method = RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> editNotes(@RequestParam(value="token") String token,@RequestParam(value="noteId") String noteId ,@RequestBody EditNoteDto editNoteDto) throws TokenParsingException, EditDtoException, NoteNotFoundException, UnauthorizedUserException {
		
		notesService.editNote(token,editNoteDto,noteId);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Notes Updated");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	//------------------Delete Notes---------------------------
	
	@RequestMapping(value="/delete",method = RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> deleteNote(@RequestParam(value="token") String token,@RequestParam(value="noteId") String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException{
		
		notesService.delete(token,noteId);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Note Trashed");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	//--------------------Open A Note------------------------------
	@RequestMapping(value="/open",method=RequestMethod.POST)
	public ResponseEntity<ViewNoteDto> openNote(@RequestParam(value="token") String token,@RequestParam(value="noteId") String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException{
		
		ViewNoteDto viewNote = notesService.openNote(token,noteId);
	
		return new ResponseEntity<>(viewNote,HttpStatus.OK);
		
	}
	
	//-------------------Delete Forever---------------------------
	
	@RequestMapping(value="/deleteforever",method=RequestMethod.DELETE)
	public ResponseEntity<NoteResponseDto> deleteForever(@RequestParam(value="token") String token,@RequestParam(value="noteId") String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException, NoteNotTrashedException{
		
		notesService.deleteForever(token,noteId);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Deleted Permanently");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	//------------------Restore-------------------------------------
	
	@RequestMapping(value="/restore",method=RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> restore(@RequestParam(value="token") String token,@RequestParam(value="noteId") String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException, NoteNotTrashedException{
		
		notesService.restore(token,noteId);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Note Restored");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	//-------------------Set Reminder------------------------------
	@RequestMapping(value="/addreminder",method=RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> setReminder(@RequestParam(value="token") String token, @RequestParam(value="noteId") String noteId,@RequestBody DateDto dateDto) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException, ParseException{
		
		notesService.reminder(token,noteId,dateDto);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Reminder Added");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	//-------------------Unset reminder---------------------------
	
	@RequestMapping(value="/removereminder",method=RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> removeReminder(@RequestParam(value="token") String token, @RequestParam(value="noteId") String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException{
		
		notesService.unsetReminder(token,noteId);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Reminder Removed");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value="/createlabel",method=RequestMethod.POST)
	public ResponseEntity<NoteResponseDto> createLabel(@RequestParam(value="token") String token,@RequestBody String labelName) throws TokenParsingException, LabelException{
		notesService.createLable(token, labelName);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Label Created");
		response.setStatus(1);

		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	@RequestMapping(value="/addlabel",method=RequestMethod.POST)
	public ResponseEntity<NoteResponseDto> addLabel(@RequestParam(value="token") String token, @RequestParam(value="noteId") String noteId, @RequestBody String labelName) throws TokenParsingException, NoteNotFoundException, LabelException{
		
		notesService.addLable(token,noteId,labelName);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Label added To Note");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	@RequestMapping(value="/removelabel",method=RequestMethod.POST)
	public ResponseEntity<NoteResponseDto> removeLabel(@RequestParam(value="token") String token, @RequestParam(value="noteId") String noteId, @RequestBody String labelName) throws TokenParsingException, NoteNotFoundException, LabelException{
		
		notesService.removeLabel(token,noteId,labelName);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Label Removed");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
}
