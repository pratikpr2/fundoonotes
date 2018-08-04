package com.bridgelabz.fundoonotes.notes.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.notes.exceptions.CreateDtoException;
import com.bridgelabz.fundoonotes.notes.exceptions.EditDtoException;
import com.bridgelabz.fundoonotes.notes.exceptions.InvalidDateFormatException;
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
import com.bridgelabz.fundoonotes.user.exception.MalformedLinkException;
import com.bridgelabz.fundoonotes.user.exception.TokenParsingException;

@RestController
@RequestMapping("/api/notes")
public class NotesController {

	@Autowired
	private NotesService notesService;
	
	//------------------Create Note---------------------------
	/**
	 * 
	 * @param req
	 * @param createDto
	 * @param isPinned
	 * @param isArchived
	 * @return Created Note
	 * @throws CreateDtoException
	 * @throws TokenParsingException
	 * @throws InvalidDateFormatException
	 * @throws LabelException
	 */
	@RequestMapping(value="/create",method= RequestMethod.POST)
	public ResponseEntity<ViewNoteDto > createNote(HttpServletRequest req,@RequestBody CreateDTO createDto,@RequestParam(value="isPinned")boolean isPinned,@RequestParam(value="isArchived")boolean isArchived) throws CreateDtoException, TokenParsingException, InvalidDateFormatException, LabelException {
		
		String userId = (String)req.getAttribute("userId");
		
		ViewNoteDto view = notesService.create(createDto, userId, isPinned, isArchived);
	
		return new ResponseEntity<>(view,HttpStatus.OK);
		
	}
	
	//-------------------Open Notes---------------------------
	/**
	 * 
	 * @param req
	 * @return List of Notes
	 * @throws TokenParsingException
	 * @throws NoteNotFoundException
	 */
	@RequestMapping(value="/openAll",method = RequestMethod.GET)
	public ResponseEntity<List<ViewNoteDto>> openNotes(HttpServletRequest req) throws TokenParsingException, NoteNotFoundException{
		
		String userId = (String)req.getAttribute("userId");

		List<ViewNoteDto> notes = notesService.openAllNotes(userId);
		
		return new ResponseEntity<List<ViewNoteDto>>(notes,HttpStatus.OK);
	}
	
	//------------------Edit Notes-----------------------------
	/**
	 * 
	 * @param req
	 * @param noteId
	 * @param editNoteDto
	 * @return Edit Response
	 * @throws TokenParsingException
	 * @throws EditDtoException
	 * @throws NoteNotFoundException
	 * @throws UnauthorizedUserException
	 */
	@RequestMapping(value="/edit/{noteId}",method = RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> editNotes(HttpServletRequest req,@PathVariable String noteId ,@RequestBody EditNoteDto editNoteDto) throws TokenParsingException, EditDtoException, NoteNotFoundException, UnauthorizedUserException {
		
		String userId = (String)req.getAttribute("userId");
		
		notesService.editNote(userId,editNoteDto,noteId);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Notes Updated");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	//------------------Trash/Restore Notes---------------------------
	/**
	 * 
	 * @param req
	 * @param noteId
	 * @param condition
	 * @return Trash Response
	 * @throws TokenParsingException
	 * @throws NoteNotFoundException
	 * @throws UnauthorizedUserException
	 */
	
	@RequestMapping(value="/trash/{noteId}",method = RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> deleteNote(HttpServletRequest req,@PathVariable String noteId,@RequestParam(value="condition") boolean condition) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException{
		
		String userId = (String)req.getAttribute("userId");
		
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
	/**
	 * 
	 * @param req
	 * @param noteId
	 * @return Delete Response
	 * @throws TokenParsingException
	 * @throws NoteNotFoundException
	 * @throws UnauthorizedUserException
	 * @throws NoteNotTrashedException
	 */
	
	@RequestMapping(value="/deleteforever/{noteId}",method=RequestMethod.DELETE)
	public ResponseEntity<NoteResponseDto> deleteForever(HttpServletRequest req,@PathVariable String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException, NoteNotTrashedException{
		
		String userId = (String)req.getAttribute("userId");
		
		notesService.deleteForever(userId,noteId);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Deleted Permanently");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	//-------------------Set Reminder------------------------------
	/**
	 * 
	 * @param req
	 * @param noteId
	 * @param dateDto
	 * @return Reminder Response
	 * @throws TokenParsingException
	 * @throws NoteNotFoundException
	 * @throws UnauthorizedUserException
	 * @throws InvalidDateFormatException
	 */
	@RequestMapping(value="/addreminder/{noteId}",method=RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> setReminder(HttpServletRequest req, @PathVariable String noteId,@RequestBody DateDto dateDto) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException, InvalidDateFormatException{
		
		String userId = (String)req.getAttribute("userId");
		
		notesService.reminder(userId, noteId, dateDto);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Reminder Added");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	//-------------------Unset reminder---------------------------
	/**
	 * 
	 * @param req
	 * @param noteId
	 * @return Reminder response
	 * @throws TokenParsingException
	 * @throws NoteNotFoundException
	 * @throws UnauthorizedUserException
	 */
	
	@RequestMapping(value="/removereminder",method=RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> removeReminder(HttpServletRequest req, @RequestParam(value="noteId") String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException{
		
		String userId = (String)req.getAttribute("userId");
		
		notesService.unsetReminder(userId,noteId);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Reminder Removed");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	
	
	//----------------------Add Labels---------------------------
	/**
	 * 
	 * @param req
	 * @param noteId
	 * @param labelName
	 * @return Label Response
	 * @throws TokenParsingException
	 * @throws NoteNotFoundException
	 * @throws LabelException
	 * @throws UnauthorizedUserException
	 */
	@RequestMapping(value="/addlabel/{noteId}",method=RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> addLabel(HttpServletRequest req, @PathVariable String noteId, @RequestBody String labelName) throws TokenParsingException, NoteNotFoundException, LabelException, UnauthorizedUserException{
		
		String userId = (String)req.getAttribute("userId");
		
		notesService.addLable(userId,noteId,labelName);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Label added To Note");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	//-----------------------Remove Label--------------------------
	/**
	 * 
	 * @param req
	 * @param noteId
	 * @param labelName
	 * @return Label Response
	 * @throws TokenParsingException
	 * @throws NoteNotFoundException
	 * @throws LabelException
	 * @throws UnauthorizedUserException
	 */
	@RequestMapping(value="/removelabel/{noteId}",method=RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> removeLabel(HttpServletRequest req, @PathVariable String noteId, @RequestBody String labelName) throws TokenParsingException, NoteNotFoundException, LabelException, UnauthorizedUserException{
		
		String userId = (String)req.getAttribute("userId");
		
		notesService.removeLabel(userId,noteId,labelName);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Label Removed");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	

	
	//-----------------------Archive/UnArchive Note----------------------------
	/**
	 * 
	 * @param req
	 * @param noteId
	 * @param condition
	 * @return Archive Response
	 * @throws TokenParsingException
	 * @throws NoteNotFoundException
	 * @throws UnauthorizedUserException
	 */
	@RequestMapping(value="/archive/{noteId}",method= RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> archive(HttpServletRequest req,@RequestParam String noteId,@RequestParam boolean condition) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException{
		
		String userId = (String)req.getAttribute("userId");
			
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
	/**
	 * 
	 * @param req
	 * @param noteId
	 * @param condition
	 * @return Pin Response
	 * @throws TokenParsingException
	 * @throws NoteNotFoundException
	 * @throws UnauthorizedUserException
	 */
	@RequestMapping(value="/pin/{noteId}",method= RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> pin(HttpServletRequest req,@RequestParam String noteId,@RequestParam boolean condition) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException{
		
		
		String userId = (String)req.getAttribute("userId");
		
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
	
	//-------------------View Labeled Notes-------------------------
	/**
	 * 
	 * @param req
	 * @param labelId
	 * @return List of Labels
	 * @throws LabelException
	 * @throws NoteNotFoundException
	 */
	@RequestMapping(value="/ViewNoteByLabel/{labelId}",method= RequestMethod.GET)
	public ResponseEntity<List<ViewNoteDto>> viewLabeledNotes(HttpServletRequest req,@PathVariable String labelId) throws LabelException, NoteNotFoundException{
		
		String userId = (String)req.getAttribute("userId");
		
		List<ViewNoteDto> viewNoteList = notesService.viewLabeledNotes(userId,labelId);
		
		return new ResponseEntity<>(viewNoteList,HttpStatus.OK);
	}
	//-------------------Add Color -------------------------
	/**
	 * 
	 * @param req
	 * @param color
	 * @param noteId
	 * @return Color Response
	 * @throws NoteNotFoundException
	 * @throws UnauthorizedUserException
	 */
	@RequestMapping(value="/addcolor/{noteId}",method= RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> addcolor(HttpServletRequest req,@RequestParam(value="color") String color,@PathVariable String noteId) throws NoteNotFoundException, UnauthorizedUserException{
		
		String userId = (String)req.getAttribute("userId");
		
		notesService.addColor(userId,color,noteId);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Color Added");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	//-------------------Remove Color -------------------------
	/**
	 * 
	 * @param req
	 * @param noteId
	 * @return
	 * @throws NoteNotFoundException
	 * @throws UnauthorizedUserException
	 */
	@RequestMapping(value="/removecolor/{noteId}",method= RequestMethod.PUT)
	public ResponseEntity<NoteResponseDto> removecolor(HttpServletRequest req,@PathVariable String noteId) throws NoteNotFoundException, UnauthorizedUserException{
		
		String userId = (String)req.getAttribute("userId");
		
		notesService.removeColor(userId,noteId);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Color Added");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	//---------------------View Pinned Notes--------------------------
	/**
	 * 
	 * @param req
	 * @param labelId
	 * @return List of Pinned Notes
	 * @throws LabelException
	 * @throws NoteNotFoundException
	 */
	@RequestMapping(value="/ViewPinnedNotes",method= RequestMethod.GET)
	public ResponseEntity<List<ViewNoteDto>> viewPinnedNotes(HttpServletRequest req) throws NoteNotFoundException{
		
		String userId = (String)req.getAttribute("userId");
		
		List<ViewNoteDto> viewNoteList = notesService.viewPinnedNotes(userId);
		
		return new ResponseEntity<>(viewNoteList,HttpStatus.OK);
	}
	
	//--------------------View Archived Notes---------------------------
	/**
	 * 
	 * @param req
	 * @param labelId
	 * @return
	 * @throws LabelException
	 * @throws NoteNotFoundException
	 */
	@RequestMapping(value="/ViewArchivedNotes",method= RequestMethod.GET)
	public ResponseEntity<List<ViewNoteDto>> viewArchivedNotes(HttpServletRequest req) throws NoteNotFoundException{
		
		String userId = (String)req.getAttribute("userId");
		
		List<ViewNoteDto> viewNoteList = notesService.viewArchivedNotes(userId);
		
		return new ResponseEntity<>(viewNoteList,HttpStatus.OK);
	}
	/**
	 * @param req
	 * @param noteId
	 * @param link
	 * @return ViewNoteDto
	 * @throws NoteNotFoundException
	 * @throws UnauthorizedUserException
	 * @throws MalformedLinkException 
	 */
	@RequestMapping(value="/AddLink",method= RequestMethod.PUT)
	public ResponseEntity<ViewNoteDto> addLink(HttpServletRequest req,@PathVariable String noteId,@RequestBody String link) throws NoteNotFoundException, UnauthorizedUserException, MalformedLinkException{
		
		String userId = (String)req.getAttribute("userId");
		
		ViewNoteDto viewnoteDto = notesService.addLink(userId, noteId, link);
		
		return new ResponseEntity<>(viewnoteDto,HttpStatus.OK);
	}
}
