package com.bridgelabz.fundoonotes.notes.services;

import java.util.List;

import com.bridgelabz.fundoonotes.notes.exceptions.CreateDtoException;
import com.bridgelabz.fundoonotes.notes.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonotes.notes.model.CreateDTO;
import com.bridgelabz.fundoonotes.notes.model.Note;
import com.bridgelabz.fundoonotes.notes.model.ViewNoteDto;
import com.bridgelabz.fundoonotes.user.exception.TokenParsingException;

public interface NotesService {

	public ViewNoteDto create(CreateDTO createDto, String userId) throws CreateDtoException, TokenParsingException;
	public ViewNoteDto open(String userId) throws TokenParsingException, NoteNotFoundException;
	public void delete();
	public void edit();
	public List<ViewNoteDto> openAllNotes(String token) throws TokenParsingException, NoteNotFoundException;
	
}
