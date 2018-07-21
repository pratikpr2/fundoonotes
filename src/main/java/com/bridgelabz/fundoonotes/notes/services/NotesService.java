package com.bridgelabz.fundoonotes.notes.services;

import java.text.ParseException;
import java.util.List;

import com.bridgelabz.fundoonotes.notes.exceptions.CreateDtoException;
import com.bridgelabz.fundoonotes.notes.exceptions.EditDtoException;
import com.bridgelabz.fundoonotes.notes.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonotes.notes.exceptions.NoteNotTrashedException;
import com.bridgelabz.fundoonotes.notes.exceptions.UnauthorizedUserException;
import com.bridgelabz.fundoonotes.notes.model.CreateDTO;
import com.bridgelabz.fundoonotes.notes.model.DateDto;
import com.bridgelabz.fundoonotes.notes.model.EditNoteDto;
import com.bridgelabz.fundoonotes.notes.model.ViewNoteDto;
import com.bridgelabz.fundoonotes.user.exception.TokenParsingException;

public interface NotesService {

	public ViewNoteDto create(CreateDTO createDto, String userId) throws CreateDtoException, TokenParsingException;
	
	//public ViewNoteDto open(String userId) throws TokenParsingException, NoteNotFoundException;
	
	public List<ViewNoteDto> openAllNotes(String token) throws TokenParsingException, NoteNotFoundException;
	
	public void editNote(String token, EditNoteDto editNoteDto, String noteId) throws TokenParsingException, EditDtoException, NoteNotFoundException, UnauthorizedUserException;
	
	void delete(String token, String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException;
	
	public ViewNoteDto openNote(String token, String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException;
	
	public void deleteForever(String token, String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException, NoteNotTrashedException;
	
	public void restore(String token, String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException, NoteNotTrashedException;
	
	public void reminder(String token, String noteId,DateDto dateDto) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException, ParseException;

	public void unsetReminder(String token, String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException;

	
	
}
