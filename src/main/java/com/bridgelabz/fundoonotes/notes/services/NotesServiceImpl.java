package com.bridgelabz.fundoonotes.notes.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.SystemPropertyUtils;

import com.bridgelabz.fundoonotes.notes.exceptions.CreateDtoException;
import com.bridgelabz.fundoonotes.notes.exceptions.EditDtoException;
import com.bridgelabz.fundoonotes.notes.exceptions.LabelException;
import com.bridgelabz.fundoonotes.notes.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonotes.notes.exceptions.NoteNotTrashedException;
import com.bridgelabz.fundoonotes.notes.exceptions.UnauthorizedUserException;
import com.bridgelabz.fundoonotes.notes.model.CreateDTO;
import com.bridgelabz.fundoonotes.notes.model.DateDto;
import com.bridgelabz.fundoonotes.notes.model.EditNoteDto;
import com.bridgelabz.fundoonotes.notes.model.Label;
import com.bridgelabz.fundoonotes.notes.model.Note;
import com.bridgelabz.fundoonotes.notes.model.ViewNoteDto;
import com.bridgelabz.fundoonotes.notes.repositories.LabelRepository;
import com.bridgelabz.fundoonotes.notes.repositories.NotesRepository;
import com.bridgelabz.fundoonotes.notes.utility.NotesUtil;
import com.bridgelabz.fundoonotes.user.exception.TokenParsingException;
import com.bridgelabz.fundoonotes.user.token.JwtToken;

@Service
public class NotesServiceImpl implements NotesService {

	@Autowired 
	NotesRepository notesrepo;
	
	@Autowired
	LabelRepository labelrepo;
	
	@Autowired
	JwtToken jwt;

	/**
	 * To Create A New Note
	 * 
	 * @param createDto
	 * @param token
	 * @return ViewNoteDto
	 * @throws CreateDtoException
	 * @throws TokenParsingException
	 */
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
		if(!(createDto.getColor().length()==0 || createDto.getColor().trim().length()==0))
			note.setColor(createDto.getColor());
		notesrepo.save(note);
		
		ViewNoteDto viewNote = new ViewNoteDto();
		
		viewNote.setBody(note.getBody());
		viewNote.setTitle(note.getTitle());
		viewNote.setCreatedAt(note.getCreatedAt());
		viewNote.setLastModified(note.getLastModified());
		viewNote.setReminder(note.getReminder());
		
		return viewNote;
	}
	
	/**
	 * To Delete A Note
	 * 
	 * @param token
	 * @throws UnauthorizedUserException
	 * @throws NoteNotFoundException
	 * @throws TokenParsingException
	 */
	@Override
	public void delete(String token,String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException {
		// TODO Auto-generated method stub
		jwt.parseJWT(token);
		
		Optional<Note> note = notesrepo.findById(noteId);
		
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Note with such Id");
		}
		if(!note.get().getUserId().equals(jwt.getUserId(token))) {
			throw new UnauthorizedUserException("UnAuthorized User");
		}
		
		note.get().setTrashed(true);
		notesrepo.save(note.get());
		
	}

	/**
	 * To Open All Notes
	 * 
	 * @param token
	 * @return List<ViewNoteDto>
	 * @throws NoteNotFoundException
	 * @throws TokenParsingException
	 */
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
			if(!note.get(i).isTrashed()) {
				ViewNoteDto notes =  new ViewNoteDto();
				notes.setBody(note.get(i).getBody());
				notes.setTitle(note.get(i).getTitle());
				notes.setCreatedAt(note.get(i).getCreatedAt());
				notes.setLastModified(note.get(i).getLastModified());
				notes.setReminder(note.get(i).getReminder());
			
				notesList.add(notes);
			}
		}
		if(notesList.isEmpty()) {
			throw new NoteNotFoundException("No Notes Found");
		}
		return notesList;
	}
	/**
	 * To Edit A Note
	 * 
	 * @param token
	 * @param noteId
	 * @param editNoteDto
	 * @throws EditDtoException
	 * @throws UnauthorizedUserException
	 * @throws NoteNotFoundException
	 * @throws TokenParsingException
	 */
	@Override
	public void editNote(String token, EditNoteDto editNoteDto,String noteId) throws TokenParsingException, EditDtoException, NoteNotFoundException, UnauthorizedUserException {
		// TODO Auto-generated method stub
		
		//NotesUtil.validateEditNoteDto(editNoteDto);
		
		jwt.parseJWT(token);
		
		Optional<Note> note = notesrepo.findById(noteId);
		if(!note.isPresent() || note.get().isTrashed()) {
			throw new NoteNotFoundException("No Notes with such Id found");
		}
		if(!note.get().getUserId().equals(jwt.getUserId(token))) {
			throw new UnauthorizedUserException("UnAuthorized User");
		}
		note.get().setTitle(editNoteDto.getTitle());
		note.get().setBody(editNoteDto.getBody());
		note.get().setReminder(editNoteDto.getReminder());
		note.get().setColor(editNoteDto.getColor());
		note.get().setLastModified(NotesUtil.generateDate());
		
		notesrepo.save(note.get());
	}
	/**
	 * To View A Note
	 * 
	 * @param token
	 * @param noteId
	 * @return ViewNoteDto
	 * @throws UnauthorizedUserException
	 * @throws NoteNotFoundException
	 * @throws TokenParsingException
	 */
	@Override
	public ViewNoteDto openNote(String token, String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException {
		// TODO Auto-generated method stub
		jwt.parseJWT(token);
		
		Optional<Note> note = notesrepo.findById(noteId);
		
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes With such Id");
		}
		if(!note.get().getUserId().equals(jwt.getUserId(token))) {
			throw new UnauthorizedUserException("UnAuthorized User");
		}
		
		ViewNoteDto viewNote = new ViewNoteDto();
		viewNote.setBody(note.get().getBody());
		viewNote.setTitle(note.get().getTitle());
		viewNote.setCreatedAt(note.get().getCreatedAt());
		viewNote.setLastModified(note.get().getLastModified());
		viewNote.setReminder(note.get().getReminder());
		
		return viewNote;
	}
	/**
	 * To Delete A Note
	 * 
	 * @param token
	 * @param noteId
	 * @throws UnauthorizedUserException
	 * @throws NoteNotFoundException
	 * @throws TokenParsingException
	 * @throws NoteNotTrashedException
	 */
	@Override
	public void deleteForever(String token, String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException, NoteNotTrashedException {
		// TODO Auto-generated method stub
		jwt.parseJWT(token);
		
		Optional<Note> note = notesrepo.findById(noteId);
		
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes With such Id");
		}
		if(!note.get().getUserId().equals(jwt.getUserId(token))) {
			throw new UnauthorizedUserException("UnAuthorized User");
		}
		if(!note.get().isTrashed()) {
			throw new NoteNotTrashedException("No Notes To Delete");
		}
		
		notesrepo.delete(note.get());
		
	}
	/**
	 * To Restore A Note
	 * 
	 * @param token
	 * @param noteId
	 * @throws UnauthorizedUserException
	 * @throws NoteNotFoundException
	 * @throws TokenParsingException
	 * @throws NoteNotTrashedException
	 */
	@Override
	public void restore(String token, String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException, NoteNotTrashedException {
		// TODO Auto-generated method stub
		
		jwt.parseJWT(token);
		
		Optional<Note> note = notesrepo.findById(noteId);
		
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes With Such Id");
		}
		if(!note.get().getUserId().equals(jwt.getUserId(token))) {
			throw new UnauthorizedUserException("UnAuthorized User");
		}
		if(!note.get().isTrashed()) {
			throw new NoteNotTrashedException("No Notes To Restore");
		}
		
		note.get().setTrashed(false);
		
		notesrepo.save(note.get());
	}
	/**
	 * To Set A Reminder
	 * 
	 * @param token
	 * @param noteId
	 * @throws UnauthorizedUserException
	 * @throws NoteNotFoundException
	 * @throws TokenParsingException
	 * @throws ParseException
	 */
	@Override
	public void reminder(String token, String noteId,DateDto dateDto) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException, ParseException {
		// TODO Auto-generated method stub
		jwt.parseJWT(token);
		
		NotesUtil.ValidateDate(dateDto);
		Optional<Note> note = notesrepo.findById(noteId);
		
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes With Such Id");
		}
		if(!note.get().getUserId().equals(jwt.getUserId(token))) {
			throw new UnauthorizedUserException("UnAuthorized User");
		}
		if(note.get().isTrashed()) {
			throw new NoteNotFoundException("No Notes Found");
		}
		
		DateTime date = DateTime.parse(dateDto.getDay()+"/"+dateDto.getMonth()+"/"+dateDto.getYear()+" "+dateDto.getHours()+":"+dateDto.getMinutes()+":"+dateDto.getSeconds(),
				DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss"));
		
		note.get().setReminder(date.toDate());
		
		notesrepo.save(note.get());
	}
	/**
	 * To UnSet A Reminder
	 * 
	 * @param token
	 * @param noteId
	 * @throws UnauthorizedUserException
	 * @throws NoteNotFoundException
	 * @throws TokenParsingException
	 */
	@Override
	public void unsetReminder(String token, String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException {
		// TODO Auto-generated method stub
		jwt.parseJWT(token);
		
		Optional<Note> note = notesrepo.findById(noteId);
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes With Such Id");
		}
		if(!note.get().getUserId().equals(jwt.getUserId(token))) {
			throw new UnauthorizedUserException("UnAuthorized User");
		}
		if(note.get().isTrashed()) {
			throw new NoteNotFoundException("No Notes Found");
		}
		note.get().setReminder(null);
		
		notesrepo.save(note.get());
	}
	@Override
	public void createLable(String token, String lableName) throws TokenParsingException, LabelException {
		jwt.parseJWT(token);
		if((lableName.trim().length()==0 || lableName.equals(null) || lableName.replaceAll("\"","").trim().length()==0)) {
			throw new LabelException("No Label Name");
		}
		
		List<Label> lableList = labelrepo.findAllByUserId(jwt.getUserId(token));		
		
		for(int i=0;i<lableList.size();i++) {
			if(lableList.get(i).getLableName().equals(lableName)) {
				throw new LabelException("Label Already Exists");
			}
		}
		
		Label label = new Label();
		label.setUserId(jwt.getUserId(token));
		label.setLableName(lableName);
		
		labelrepo.save(label);
	}

	@Override
	public void addLable(String token, String noteId, String labelName) throws TokenParsingException, NoteNotFoundException, LabelException {
		// TODO Auto-generated method stub
		jwt.parseJWT(token);
		
		boolean flag = false;
		
		Optional<Note> note = notesrepo.findById(noteId);
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes found");
		}
		
		List<Label> labelList = new ArrayList<>();
		labelList = labelrepo.findAllByUserId(jwt.getUserId(token));
		if(labelList.isEmpty()) {
			throw new LabelException("No Labels");
		}
		
		List<Label> tempList = new ArrayList<>() ;
		
		//System.out.println(note.get().getLabelList().toString());
		tempList =  note.get().getLabelList();
		
		System.out.println(tempList.size());
		
		for(int i =0;i<labelList.size();i++) {
			if(labelList.get(i).getLableName().equals(labelName)) {
				System.out.println(labelList.get(i).getLableName());
				Label label = new Label();
				label.setLableName(labelList.get(i).getLableName());
				label.setLabelId(labelList.get(i).getLabelId());
				//label.setNoteId(labelList.get(i).getNoteId());
				//label.setUserId(labelList.get(i).getUserId());
				tempList.add(label);
				flag = true;
			}
		}
		
		if(!flag) {
			Label label = new Label();
			label.setUserId(jwt.getUserId(token));
			label.setLableName(labelName);
			labelrepo.save(label);
		}
		note.get().setLabelList(tempList);
		
		notesrepo.save(note.get());
	}

	@Override
	public void removeLabel(String token, String noteId, String labelName) throws TokenParsingException, NoteNotFoundException, LabelException {
		// TODO Auto-generated method stub
		jwt.parseJWT(token);
		boolean flag= false;
		Optional<Note> note = notesrepo.findById(noteId);
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes found");
		}
		
		
		List<Label> tempList = new ArrayList<>() ;
		
		tempList =  note.get().getLabelList();
		
		if(tempList.isEmpty()) {
			throw new LabelException("No Labels");
		}
		
		for(int i =0;i<tempList.size();i++) {
			if(tempList.get(i).getLableName().equals(labelName)) {
				tempList.remove(i);
				flag=true;
			}
		}
		if(!flag) {
			throw new LabelException("No Such Labels");
		}
		note.get().setLabelList(tempList);
		
		notesrepo.save(note.get());
			
	}
}
