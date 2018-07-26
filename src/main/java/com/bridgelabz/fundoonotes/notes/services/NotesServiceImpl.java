package com.bridgelabz.fundoonotes.notes.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
	public ViewNoteDto create(CreateDTO createDto,String userId) throws CreateDtoException, TokenParsingException {
		// TODO Auto-generated method stub
		
		NotesUtil.ValidateCreateDto(createDto);
		
		Note note = new Note();
		note.setUserId(userId);
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
	 * @param userId
	 * @param noteId
	 * @param condition
	 * @throws UnauthorizedUserException
	 * @throws NoteNotFoundException
	 * @throws TokenParsingException
	 */
	@Override
	public void trash(String userId,String noteId,boolean condition ) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException {
		// TODO Auto-generated method stub
		//jwt.parseJWT(token);
		
		Optional<Note> note = notesrepo.findById(noteId);
		
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Note with such Id");
		}
		if(!note.get().getUserId().equals(userId)) {
			throw new UnauthorizedUserException("UnAuthorized User");
		}
		if(condition)
			note.get().setTrashed(true);
		else
			note.get().setTrashed(false);
		
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
	public List<ViewNoteDto> openAllNotes(String userId) throws TokenParsingException, NoteNotFoundException {
		// TODO Auto-generated method stub
		
		//jwt.parseJWT(token);
		
		List<Note> note = notesrepo.findAllByUserId(userId);
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
	public void editNote(String userId, EditNoteDto editNoteDto,String noteId) throws TokenParsingException, EditDtoException, NoteNotFoundException, UnauthorizedUserException {
		// TODO Auto-generated method stub
		
		//NotesUtil.validateEditNoteDto(editNoteDto);
		
		//jwt.parseJWT(token);
		
		Optional<Note> note = notesrepo.findById(noteId);
		if(!note.isPresent() || note.get().isTrashed()) {
			throw new NoteNotFoundException("No Notes with such Id found");
		}
		if(!note.get().getUserId().equals(userId)) {
			throw new UnauthorizedUserException("UnAuthorized User");
		}
		
		DateDto dateDto = editNoteDto.getReminder();
		
		DateTime date = DateTime.parse(dateDto.getDay()+"/"+dateDto.getMonth()+"/"+dateDto.getYear()+" "+dateDto.getHours()+":"+dateDto.getMinutes()+":"+dateDto.getSeconds(),
				DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss"));
		
		
		
		note.get().setTitle(editNoteDto.getTitle());
		note.get().setBody(editNoteDto.getBody());
		note.get().setReminder(date.toDate());
		note.get().setColor(editNoteDto.getColor());
		note.get().setLastModified(NotesUtil.generateDate());
		
		notesrepo.save(note.get());
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
	public void deleteForever(String userId, String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException, NoteNotTrashedException {
		// TODO Auto-generated method stub
		//jwt.parseJWT(token);
		
		Optional<Note> note = notesrepo.findById(noteId);
		
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes With such Id");
		}
		if(!note.get().getUserId().equals(userId)){
			throw new UnauthorizedUserException("UnAuthorized User");
		}
		if(!note.get().isTrashed()) {
			throw new NoteNotTrashedException("No Notes To Delete");
		}
		
		notesrepo.delete(note.get());
		
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
	public void reminder(String userId, String noteId,DateDto dateDto) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException, ParseException {
		// TODO Auto-generated method stub
		//jwt.parseJWT(token);
		
		NotesUtil.ValidateDate(dateDto);
		Optional<Note> note = notesrepo.findById(noteId);
		
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes With Such Id");
		}
		if(!note.get().getUserId().equals(userId)) {
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
	public void unsetReminder(String userId, String noteId) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException {
		// TODO Auto-generated method stub
		//jwt.parseJWT(token);
		
		Optional<Note> note = notesrepo.findById(noteId);
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes With Such Id");
		}
		if(!note.get().getUserId().equals(userId)) {
			throw new UnauthorizedUserException("UnAuthorized User");
		}
		if(note.get().isTrashed()) {
			throw new NoteNotFoundException("No Notes Found");
		}
		note.get().setReminder(null);
		
		notesrepo.save(note.get());
	}
	@Override
	public void createLable(String userId, String lableName) throws TokenParsingException, LabelException {
		//jwt.parseJWT(token);
		if((lableName.trim().length()==0 || lableName.equals(null) || lableName.replaceAll("\"","").trim().length()==0)) {
			throw new LabelException("No Label Name");
		}
		
		List<Label> lableList = labelrepo.findAllByUserId(userId);		
		
		for(int i=0;i<lableList.size();i++) {
			if(lableList.get(i).getLableName().equals(lableName)) {
				throw new LabelException("Label Already Exists");
			}
		}
		
		Label label = new Label();
		label.setUserId(userId);
		label.setLableName(lableName);
		
		labelrepo.save(label);
	}

	@Override
	public void addLable(String userId, String noteId, String labelName) throws TokenParsingException, NoteNotFoundException, LabelException, UnauthorizedUserException {
		// TODO Auto-generated method stub
		//jwt.parseJWT(token);
		
		boolean flag = false;
		
		Optional<Note> note = notesrepo.findById(noteId);
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes found");
		}
		if(!note.get().getUserId().equals(userId)) {
			throw new UnauthorizedUserException("UnAuthorized User");
		}
		List<Label> labelList = new ArrayList<>();
		labelList = labelrepo.findAllByUserId(userId);
		if(labelList.isEmpty()) {
			throw new LabelException("No Labels");
		}
		List<Label> tempList = note.get().getLabelList();
		//System.out.println(note.get().getLabelList().toString());
		for(int i =0;i<labelList.size();i++) {
			if(labelList.get(i).getLableName().equals(labelName)) {
				//System.out.println(labelList.get(i).getLableName());
				Label label = new Label();
				label.setLableName(labelList.get(i).getLableName());
				label.setLabelId(labelList.get(i).getLabelId());
				tempList.add(label);
				flag = true;
			}
		}
		
		if(!flag) {
			Label label = new Label();
			label.setUserId(userId);
			label.setLableName(labelName);
			labelrepo.save(label);
		}
		note.get().setLabelList(tempList);
		
		notesrepo.save(note.get());
	}

	@Override
	public void removeLabel(String userId, String noteId, String labelName) throws TokenParsingException, NoteNotFoundException, LabelException, UnauthorizedUserException {
		// TODO Auto-generated method stub
		//jwt.parseJWT(token);
		boolean flag= false;
		Optional<Note> note = notesrepo.findById(noteId);
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes found");
		}
		
		if(!note.get().getUserId().equals(userId)) {
			throw new UnauthorizedUserException("UnAuthorized User");
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

	@Override
	public void deleteLable(String userId, String labelId) throws TokenParsingException, LabelException, NoteNotFoundException {
		// TODO Auto-generated method stub
		//jwt.parseJWT(token);
		
		Optional<Label> label = labelrepo.findById(labelId);
		if(!label.isPresent()) {
			throw new LabelException("No Such Label");
		}
		List<Note> noteList = notesrepo.findAllByUserId(userId);
		
		if(noteList.isEmpty()) {
			throw new NoteNotFoundException("No Notes Present");
		}
		
		for(int i=0;i<noteList.size();i++) {
			List<Label> labelList = noteList.get(i).getLabelList();
			if(!labelList.isEmpty()) {
				for(int j=0;j<labelList.size();j++) {
					if(labelList.get(j).getLabelId().equals(labelId)) {
						//System.out.println(labelList.get(j).getLableName());
						labelList.remove(j);
						
					}
				}
			}
			noteList.get(i).setLabelList(labelList);
			notesrepo.save(noteList.get(i));
		}
		labelrepo.deleteById(labelId);
		
	}

	@Override
	public void archive(String userId, String noteId, boolean condition) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException {
		// TODO Auto-generated method stub
		
		//jwt.parseJWT(token);
		
		Optional<Note> note = notesrepo.findById(noteId);
		
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes Present");
		}
		if(!note.get().getUserId().equals(userId)) {
			throw new UnauthorizedUserException("UnAuthorized User");
		}
		if(condition) {
			note.get().setArchived(true);
		}else {
			note.get().setArchived(false);
		}
		notesrepo.save(note.get());
	}

	@Override
	public void pin(String userId, String noteId, boolean condition) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException {
		// TODO Auto-generated method stub
		//jwt.parseJWT(token);
		
		Optional<Note> note = notesrepo.findById(noteId);
		
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes Present");
		}
		if(!note.get().getUserId().equals(userId)) {
			throw new UnauthorizedUserException("UnAuthorized User");
		}
		
		if(condition) {
			note.get().setPinned(true);
		}else {
			note.get().setPinned(false);
		}
		
		notesrepo.save(note.get());
	}
}
