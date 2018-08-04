package com.bridgelabz.fundoonotes.notes.services;

import java.io.IOException;
import java.util.List;

import com.bridgelabz.fundoonotes.notes.exceptions.CreateDtoException;
import com.bridgelabz.fundoonotes.notes.exceptions.EditDtoException;
import com.bridgelabz.fundoonotes.notes.exceptions.InvalidDateFormatException;
import com.bridgelabz.fundoonotes.notes.exceptions.LabelException;
import com.bridgelabz.fundoonotes.notes.exceptions.LabelNotFoundException;
import com.bridgelabz.fundoonotes.notes.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonotes.notes.exceptions.NoteNotTrashedException;
import com.bridgelabz.fundoonotes.notes.exceptions.UnauthorizedUserException;
import com.bridgelabz.fundoonotes.notes.model.CreateDTO;
import com.bridgelabz.fundoonotes.notes.model.DateDto;
import com.bridgelabz.fundoonotes.notes.model.EditLabelDto;
import com.bridgelabz.fundoonotes.notes.model.EditNoteDto;
import com.bridgelabz.fundoonotes.notes.model.ViewLabelDto;
import com.bridgelabz.fundoonotes.notes.model.ViewNoteDto;
import com.bridgelabz.fundoonotes.user.exception.MalformedLinkException;
import com.bridgelabz.fundoonotes.user.exception.TokenParsingException;

public interface NotesService {

	public ViewNoteDto create(CreateDTO createDto, String userId, boolean isPinned, boolean isArchived) throws CreateDtoException, TokenParsingException, InvalidDateFormatException, LabelException;
	
	public List<ViewNoteDto> openAllNotes(String token) throws TokenParsingException, NoteNotFoundException;
	
	public void editNote(String userId, EditNoteDto editNoteDto, String noteId) throws TokenParsingException, EditDtoException, NoteNotFoundException, UnauthorizedUserException;
	
	public void trash(String userId, String noteId, boolean condition) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException;
	
	public void deleteForever(String userId, String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException, NoteNotTrashedException;
	
	public void reminder(String userId, String noteId,DateDto dateDto) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException, InvalidDateFormatException;

	public void unsetReminder(String userId, String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException;

	public void addLable(String userId, String noteId, String labelId) throws TokenParsingException, NoteNotFoundException, LabelException, UnauthorizedUserException;

	public void removeLabel(String userId, String noteId, String labelName) throws TokenParsingException, NoteNotFoundException, LabelException, UnauthorizedUserException;

	public void archive(String userId, String noteId, boolean condition) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException;

	public void pin(String userId, String noteId, boolean condition) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException;

	public List<ViewNoteDto> viewLabeledNotes(String userId, String labelId) throws LabelException, NoteNotFoundException;

	public void addColor(String userId, String color, String noteId) throws NoteNotFoundException, UnauthorizedUserException;

	public void removeColor(String userId, String noteId) throws NoteNotFoundException, UnauthorizedUserException;

	public List<ViewNoteDto> viewPinnedNotes(String userId) throws NoteNotFoundException;

	public List<ViewNoteDto> viewArchivedNotes(String userId) throws NoteNotFoundException;
	
	public ViewNoteDto addLink(String userId,String noteId,String link) throws NoteNotFoundException, UnauthorizedUserException, MalformedLinkException;
}
