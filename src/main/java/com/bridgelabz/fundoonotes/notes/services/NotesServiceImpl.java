package com.bridgelabz.fundoonotes.notes.services;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.notes.exceptions.CreateDtoException;
import com.bridgelabz.fundoonotes.notes.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonotes.notes.model.CreateDTO;
import com.bridgelabz.fundoonotes.notes.model.Note;
import com.bridgelabz.fundoonotes.notes.model.ViewNoteDto;
import com.bridgelabz.fundoonotes.notes.repositories.NotesRepository;
import com.bridgelabz.fundoonotes.notes.utility.NotesUtil;
import com.bridgelabz.fundoonotes.user.exception.TokenParsingException;
import com.bridgelabz.fundoonotes.user.token.JwtToken;

@Service
public class NotesServiceImpl implements NotesService {

	@Autowired 
	NotesRepository notesrepo;
	
	@Autowired
	JwtToken jwt;

	
	@Override
	public ViewNoteDto create(CreateDTO createDto,String token) throws CreateDtoException, TokenParsingException {
		// TODO Auto-generated method stub
		jwt.parseJWT(token);
		NotesUtil.ValidateCreateDto(createDto);
		
		Note note = new Note();
		note.setUserId(jwt.getUserId(token));
		note.setTitle(createDto.getTitle());
		note.setBody(createDto.getBody());
		note.setCreatedAt(NotesUtil.generateDate());
		note.setLastModified(NotesUtil.generateDate());
		note.setReminder(createDto.getReminder());
		
		notesrepo.save(note);
		
		ViewNoteDto viewNote = new ViewNoteDto();
		
		viewNote.setBody(note.getBody());
		viewNote.setTitle(note.getTitle());
		viewNote.setCreatedAt(note.getCreatedAt());
		viewNote.setLastModified(note.getLastModified());
		viewNote.setReminder(note.getReminder());
		
		return viewNote;
	}

	@Override
	public ViewNoteDto open(String token) throws TokenParsingException, NoteNotFoundException {
		// TODO Auto-generated method stub
		jwt.parseJWT(token);
		
		Optional<Note> note = notesrepo.findByUserId(jwt.getUserId(token));
		
		if(!note.isPresent()) {
			throw new NoteNotFoundException("Note Not Found");
		}
		
		ViewNoteDto viewNote = new ViewNoteDto();
		
		viewNote.setTitle(note.get().getTitle());
		viewNote.setBody(note.get().getBody());
		viewNote.setCreatedAt(note.get().getCreatedAt());
		viewNote.setLastModified(note.get().getLastModified());
		viewNote.setReminder(note.get().getReminder());
		
		
		return viewNote;
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void edit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ViewNoteDto> openAllNotes(String token) throws TokenParsingException, NoteNotFoundException {
		// TODO Auto-generated method stub
		
		jwt.parseJWT(token);
		
		List<Note> note = notesrepo.findAllByUserId(jwt.getUserId(token));
		List<ViewNoteDto> notesList = new ArrayList<>();
		if(note.isEmpty()) {
			throw new NoteNotFoundException("No Notes Found");
		}
		
		
		
		for(int i=0;i<note.size();i++) {
			
			ViewNoteDto notes =  new ViewNoteDto();
			notes.setBody(note.get(i).getBody());
			notes.setTitle(note.get(i).getTitle());
			notes.setCreatedAt(note.get(i).getCreatedAt());
			notes.setLastModified(note.get(i).getLastModified());
			notes.setReminder(note.get(i).getReminder());
			
			notesList.add(notes);
		}
		
		return notesList;
	}

}
