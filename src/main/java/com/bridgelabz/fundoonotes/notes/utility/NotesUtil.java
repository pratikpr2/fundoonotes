package com.bridgelabz.fundoonotes.notes.utility;

import java.util.Date;

import com.bridgelabz.fundoonotes.notes.exceptions.CreateDtoException;
import com.bridgelabz.fundoonotes.notes.model.CreateDTO;

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
	}
	
}
