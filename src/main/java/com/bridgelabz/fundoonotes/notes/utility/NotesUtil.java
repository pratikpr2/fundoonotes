package com.bridgelabz.fundoonotes.notes.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.bridgelabz.fundoonotes.notes.exceptions.CreateDtoException;
import com.bridgelabz.fundoonotes.notes.exceptions.EditDtoException;
import com.bridgelabz.fundoonotes.notes.model.CreateDTO;
import com.bridgelabz.fundoonotes.notes.model.DateDto;
import com.bridgelabz.fundoonotes.notes.model.EditNoteDto;

public class NotesUtil {

	public static Date generateDate() {
		Date date = new Date();
		return date;
	}
	
	public static void ValidateCreateDto(CreateDTO createDto) throws CreateDtoException {
		if(createDto.getTitle()==null && createDto.getBody()==null) {
			throw new CreateDtoException("Provide Valid Title Or Body");
		}
		if(createDto.getTitle().length()==0 && createDto.getBody().length()==0) {
			throw new CreateDtoException("Title And Body Cannot Be Empty");
		}
		if(createDto.getTitle().trim().length()==0 && createDto.getBody().trim().length()==0) {
			throw new CreateDtoException("Provide Valid Title or Body");
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

	public static void ValidateDate(DateDto dateDto) throws ParseException {
		// TODO Auto-generated method stub
		String date = dateDto.getYear()+"/"+dateDto.getMonth()+"/"+dateDto.getDay();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		sdf.setLenient(false);
		
		sdf.parse(date);
		
	}
	
}
