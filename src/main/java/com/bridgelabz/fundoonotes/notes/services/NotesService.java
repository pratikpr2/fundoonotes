package com.bridgelabz.fundoonotes.notes.services;

import java.text.ParseException;
import java.util.List;

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
import com.bridgelabz.fundoonotes.notes.model.ViewLabelDto;
import com.bridgelabz.fundoonotes.notes.model.ViewNoteDto;
import com.bridgelabz.fundoonotes.user.exception.TokenParsingException;

public interface NotesService {

	public ViewNoteDto create(CreateDTO createDto, String userId, boolean isPinned, boolean isArchived) throws CreateDtoException, TokenParsingException, InvalidDateFormatException, LabelException;
	
	//public ViewNoteDto open(String userId) throws TokenParsingException, NoteNotFoundException;
	
	public List<ViewNoteDto> openAllNotes(String token) throws TokenParsingException, NoteNotFoundException;
	
	public void editNote(String userId, EditNoteDto editNoteDto, String noteId) throws TokenParsingException, EditDtoException, NoteNotFoundException, UnauthorizedUserException;
	
	public void trash(String userId, String noteId, boolean condition) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException;
	
	
	public void deleteForever(String userId, String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException, NoteNotTrashedException;
	
	public void reminder(String userId, String noteId,DateDto dateDto) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException, InvalidDateFormatException;

	public void unsetReminder(String userId, String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException;

	public void createLable(String userId, String lableName) throws TokenParsingException, LabelException;

	public void addLable(String userId, String noteId, String labelId) throws TokenParsingException, NoteNotFoundException, LabelException, UnauthorizedUserException;

	public void removeLabel(String userId, String noteId, String labelName) throws TokenParsingException, NoteNotFoundException, LabelException, UnauthorizedUserException;

	public void deleteLable(String userId, String labelId) throws TokenParsingException, LabelException, NoteNotFoundException;

	public void archive(String userId, String noteId, boolean condition) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException;

	public void pin(String userId, String noteId, boolean condition) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException;

	public List<ViewLabelDto> viewAllLabels(String userId) throws LabelException;

	public List<ViewNoteDto> viewLabeledNotes(String userId, String labelId) throws LabelException, NoteNotFoundException;
	
}
