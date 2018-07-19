package com.bridgelabz.fundoonotes.notes.services;

import com.bridgelabz.fundoonotes.notes.exceptions.CreateDtoException;
import com.bridgelabz.fundoonotes.notes.model.CreateDTO;

public interface NotesService {

	public void create(CreateDTO createDto) throws CreateDtoException;
	public void open();
	public void delete();
	public void edit();
	
}
