package com.bridgelabz.fundoonotes.notes.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.bridgelabz.fundoonotes.notes.model.ViewLabelDto;
import com.bridgelabz.fundoonotes.notes.model.ViewNoteDto;
import com.bridgelabz.fundoonotes.notes.services.NotesService;
import com.bridgelabz.fundoonotes.user.exception.TokenParsingException;

@RestController
@RequestMapping("/notes")
public class NotesController {

	@Autowired
	NotesService notesService;
	
	//------------------Create Note---------------------------
	
	@RequestMapping(value="/create",method= RequestMethod.POST)
	public ResponseEntity<ViewNoteDto > createNote(HttpServletRequest req,@RequestBody CreateDTO createDto) throws CreateDtoException, TokenParsingException, NoteNotFoundException{
		
		String userId = (String)req.getAttribute("token");
		
		ViewNoteDto view = notesService.create(createDto,userId);
	
		return new ResponseEntity<>(view,HttpStatus.OK);
		
	}
	
	//-------------------Open Notes---------------------------
	@RequestMapping(value="/openAll",method = RequestMethod.POST)
	public ResponseEntity<List<ViewNoteDto>> openNotes(HttpServletRequest req) throws TokenParsingException, NoteNotFoundException{
		
		String userId = (String)req.getAttribute("token");

		List<ViewNoteDto> notes = notesService.openAllNotes(userId);
		
		return new ResponseEntity<List<ViewNoteDto>>(notes,HttpStatus.OK);
	}
	
	//------------------Edit Notes-----------------------------
	@RequestMapping(value="/edit",method = RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> editNotes(HttpServletRequest req,@RequestParam(value="noteId") String noteId ,@RequestBody EditNoteDto editNoteDto) throws TokenParsingException, EditDtoException, NoteNotFoundException, UnauthorizedUserException {
		
		String userId = (String)req.getAttribute("token");
		
		notesService.editNote(userId,editNoteDto,noteId);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Notes Updated");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	//------------------Trash/Restore Notes---------------------------
	
	@RequestMapping(value="/trash",method = RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> deleteNote(HttpServletRequest req,@RequestParam(value="noteId") String noteId,@RequestParam(value="condition") boolean condition) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException{
		
		String userId = (String)req.getAttribute("token");
		
		notesService.trash(userId,noteId,condition);
		
		NoteResponseDto response = new NoteResponseDto();
		if(condition) {
			response.setMessage("Note Trashed");
			response.setStatus(1);
		}
		else {
			response.setMessage("Note Restored");
			response.setStatus(1);
		}
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	
	//-------------------Delete Forever---------------------------
	
	@RequestMapping(value="/deleteforever",method=RequestMethod.DELETE)
	public ResponseEntity<NoteResponseDto> deleteForever(HttpServletRequest req,@RequestParam(value="noteId") String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException, NoteNotTrashedException{
		
		String userId = (String)req.getAttribute("token");
		
		notesService.deleteForever(userId,noteId);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Deleted Permanently");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	//-------------------Set Reminder------------------------------
	@RequestMapping(value="/addreminder",method=RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> setReminder(HttpServletRequest req, @RequestParam(value="noteId") String noteId,@RequestBody DateDto dateDto) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException, ParseException{
		
		String userId = (String)req.getAttribute("token");
		
		notesService.reminder(userId,noteId,dateDto);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Reminder Added");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	//-------------------Unset reminder---------------------------
	
	@RequestMapping(value="/removereminder",method=RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> removeReminder(HttpServletRequest req, @RequestParam(value="noteId") String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException{
		
		String userId = (String)req.getAttribute("token");
		
		notesService.unsetReminder(userId,noteId);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Reminder Removed");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	//---------------------Create Label---------------------------
	
	@RequestMapping(value="/createlabel",method=RequestMethod.POST)
	public ResponseEntity<NoteResponseDto> createLabel(HttpServletRequest req,@RequestBody String labelName) throws TokenParsingException, LabelException{
		
		String userId = (String)req.getAttribute("token");
		
		notesService.createLable(userId, labelName);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Label Created");
		response.setStatus(1);

		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	//----------------------Add Labels---------------------------
	@RequestMapping(value="/addlabel",method=RequestMethod.POST)
	public ResponseEntity<NoteResponseDto> addLabel(HttpServletRequest req, @RequestParam(value="noteId") String noteId, @RequestBody String labelName) throws TokenParsingException, NoteNotFoundException, LabelException, UnauthorizedUserException{
		
		String userId = (String)req.getAttribute("token");
		
		notesService.addLable(userId,noteId,labelName);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Label added To Note");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	//-----------------------Remove Label--------------------------
	@RequestMapping(value="/removelabel",method=RequestMethod.POST)
	public ResponseEntity<NoteResponseDto> removeLabel(HttpServletRequest req, @RequestParam(value="noteId") String noteId, @RequestBody String labelName) throws TokenParsingException, NoteNotFoundException, LabelException, UnauthorizedUserException{
		
		String userId = (String)req.getAttribute("token");
		
		notesService.removeLabel(userId,noteId,labelName);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Label Removed");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	//-----------------------Edit Label----------------------------
	@RequestMapping(value="/deletelabel",method= RequestMethod.POST)
	public ResponseEntity<NoteResponseDto> editLable(HttpServletRequest req, @RequestParam(value="labelId") String labelId) throws TokenParsingException, LabelException, NoteNotFoundException{
		
		String userId = (String)req.getAttribute("token");
		
		notesService.deleteLable(userId,labelId);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Label Deleted");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	//-----------------------Archive/UnArchive Note----------------------------
		@RequestMapping(value="/archive",method= RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> archive(HttpServletRequest req,@RequestParam String noteId,@RequestParam boolean condition) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException{
		
		String userId = (String)req.getAttribute("token");
			
		notesService.archive(userId,noteId,condition);
		
		NoteResponseDto response = new NoteResponseDto();
		if(condition) {
			
			response.setMessage("Note Archived");
			response.setStatus(1);
		}
		else {
			response.setMessage("Note Unarchived");
			response.setStatus(1);
		}
		return new ResponseEntity<>(response,HttpStatus.OK);
		
	}
	
	//-----------------------Pin/UnPin Note----------------------------
	@RequestMapping(value="/pin",method= RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> pin(HttpServletRequest req,@RequestParam String noteId,@RequestParam boolean condition) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException{
		
		
		String userId = (String)req.getAttribute("token");
		
		notesService.pin(userId,noteId,condition);
		
		NoteResponseDto response = new NoteResponseDto();
		if(condition) {
			
			response.setMessage("Note Pinned");
			response.setStatus(1);
		}
		else {
			response.setMessage("Note UnPinned");
			response.setStatus(1);
		}
		return new ResponseEntity<>(response,HttpStatus.OK);
		
	}	
	//---------------------View Label--------------------------------
	@RequestMapping(value="/ViewAllLabels",method= RequestMethod.GET)
	public ResponseEntity<List<ViewLabelDto>> viewAllLabels(HttpServletRequest req) throws LabelException{
		
		String userId = (String)req.getAttribute("token");
		
		List<ViewLabelDto> labelList=notesService.viewAllLabels(userId);
		
		return new ResponseEntity<>(labelList,HttpStatus.OK);
		
	}
	//-------------------View Labeled Notes-------------------------
	@RequestMapping(value="/ViewNoteByLabel",method= RequestMethod.GET)
	public ResponseEntity<List<ViewNoteDto>> viewLabeledNotes(HttpServletRequest req,@RequestParam(value="labelId") String labelId) throws LabelException, NoteNotFoundException{
		
		String userId = (String)req.getAttribute("token");
		
		List<ViewNoteDto> viewNoteList = notesService.viewLabeledNotes(userId,labelId);
		
		return new ResponseEntity<>(viewNoteList,HttpStatus.OK);
	}
	
}
