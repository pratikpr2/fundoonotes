package com.bridgelabz.fundoonotes.notes.utility;

import java.util.Date;

import com.bridgelabz.fundoonotes.notes.exceptions.CreateDtoException;
import com.bridgelabz.fundoonotes.notes.exceptions.EditDtoException;
import com.bridgelabz.fundoonotes.notes.model.CreateDTO;
import com.bridgelabz.fundoonotes.notes.model.EditNoteDto;

public class NotesUtil {

	public static Date generateDate() {
		Date date = new Date();
		return date;
	}
	
	public static void ValidateCreateDto(CreateDTO createDto) throws CreateDtoException {
		if(createDto.getTitle()==null || createDto.getTitle().length()==0) {
			throw new CreateDtoException("Provide A Valid title Name");
		}
		if(createDto.getBody()==null) {
			throw new CreateDtoException("Note Body Empty");
		}
		if(createDto.getColor()==null) {
			throw new CreateDtoException("Please set a color");
		}
		if(createDto.getReminder()==null) {
			throw new CreateDtoException("Please set a reminder");
		}
	}

	public static void validateEditNoteDto(EditNoteDto editNoteDto) throws EditDtoException {
		// TODO Auto-generated method stub
		if(editNoteDto.getTitle()==null || editNoteDto.getTitle().length()==0) {
			throw new EditDtoException("Invalid Title Name");
		}
		if(editNoteDto.getBody()==null || editNoteDto.getBody().length()==0) {
			throw new EditDtoException("Note Body Empty");
		}
		if(editNoteDto.getReminder()==null ) {
			throw new EditDtoException("Set a reminder");
		}
		if(editNoteDto.getColor()==null) {
			throw new EditDtoException("Please set a Color");
		}
	}
	
}
