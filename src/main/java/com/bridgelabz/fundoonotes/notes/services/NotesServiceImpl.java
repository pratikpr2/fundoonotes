package com.bridgelabz.fundoonotes.notes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.notes.exceptions.CreateDtoException;
import com.bridgelabz.fundoonotes.notes.model.CreateDTO;
import com.bridgelabz.fundoonotes.notes.model.Note;
import com.bridgelabz.fundoonotes.notes.repositories.NotesRepository;
import com.bridgelabz.fundoonotes.notes.utility.NotesUtil;
import com.bridgelabz.fundoonotes.user.token.JwtToken;

@Service
public class NotesServiceImpl implements NotesService {

	@Autowired 
	NotesRepository notesrepo;
	
	@Override
	public void create(CreateDTO createDto) throws CreateDtoException {
		// TODO Auto-generated method stub
		NotesUtil.ValidateCreateDto(createDto);
		
		Note note = new Note();
		note.setTitle(createDto.getTitle());
		note.setBody(createDto.getBody());
		note.setCreatedAt(NotesUtil.generateDate());
		note.setLastModified(NotesUtil.generateDate());
		note.setReminder(null);
		
		notesrepo.save(note);
		
		
	}

	@Override
	public void open() {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void edit() {
		// TODO Auto-generated method stub
		
	}

}
