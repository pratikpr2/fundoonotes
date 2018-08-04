package com.bridgelabz.fundoonotes.notes.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bridgelabz.fundoonotes.notes.exceptions.CreateDtoException;
import com.bridgelabz.fundoonotes.notes.exceptions.EditDtoException;
import com.bridgelabz.fundoonotes.notes.exceptions.InvalidDateFormatException;
import com.bridgelabz.fundoonotes.notes.exceptions.LabelException;
import com.bridgelabz.fundoonotes.notes.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonotes.notes.exceptions.NoteNotTrashedException;
import com.bridgelabz.fundoonotes.notes.exceptions.UnauthorizedUserException;
import com.bridgelabz.fundoonotes.notes.model.CreateDTO;
import com.bridgelabz.fundoonotes.notes.model.DateDto;
import com.bridgelabz.fundoonotes.notes.model.EditNoteDto;
import com.bridgelabz.fundoonotes.notes.model.Label;
import com.bridgelabz.fundoonotes.notes.model.Note;
import com.bridgelabz.fundoonotes.notes.model.URLMetadata;
import com.bridgelabz.fundoonotes.notes.model.ViewLabelDto;
import com.bridgelabz.fundoonotes.notes.model.ViewNoteDto;
import com.bridgelabz.fundoonotes.notes.repositories.LabelsElasticrepository;
import com.bridgelabz.fundoonotes.notes.repositories.NotesElasticRepository;
import com.bridgelabz.fundoonotes.notes.repositories.LabelRepository;
import com.bridgelabz.fundoonotes.notes.repositories.NotesRepository;
import com.bridgelabz.fundoonotes.notes.utility.NotesUtil;
import com.bridgelabz.fundoonotes.user.exception.MalformedLinkException;
import com.bridgelabz.fundoonotes.user.exception.TokenParsingException;
import com.bridgelabz.fundoonotes.user.token.JwtToken;

@Service
public class NotesServiceImpl implements NotesService {

	@Autowired 
	NotesRepository notesrepo;
	
	@Autowired
	LabelRepository labelrepo;
	
	@Autowired
	LabelsElasticrepository labelelasticrepo;
	
	@Autowired
	NotesElasticRepository notesElasticrepo;
	
	@Autowired
	ModelMapper modelMapper;
	
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
	 * @throws InvalidDateFormatException  
	 * @throws LabelException 
	 */
	@Override
	public ViewNoteDto create(CreateDTO createDto,String userId,boolean isPinned,boolean isArchived) throws CreateDtoException, TokenParsingException, InvalidDateFormatException, LabelException{
	
		NotesUtil.ValidateCreateDto(createDto);
	
		boolean flag = NotesUtil.isReminderDefault(createDto.getReminder());
		boolean flag2 = false ;
		
		Note note = new Note();
		
		note.setUserId(userId);
		note.setTitle(createDto.getTitle());
		note.setBody(createDto.getBody());
		note.setCreatedAt(NotesUtil.generateDate());
		note.setLastModified(NotesUtil.generateDate());
		
		if(!(createDto.getColor().length()==0 || createDto.getColor().trim().length()==0))
			note.setColor(createDto.getColor());
		
		List<Label> labelList = new ArrayList<>();
		labelList = labelelasticrepo.findAllByUserId(userId);
		
		List<ViewLabelDto> viewLableList = new ArrayList<>();
		
		if(!createDto.getLabel().isEmpty()) {
			for(int i=0;i<createDto.getLabel().size();i++) {
				String lableName = createDto.getLabel().get(i);
				if(!(lableName.trim().length()==0)) {
					
					if(!labelList.isEmpty()) {
						for(int j=0;j<labelList.size();j++) {
							if(labelList.get(j).getLableName().equals(lableName)) {
								
								ViewLabelDto viewLable = new ViewLabelDto();
								viewLable.setLabelId(labelList.get(j).getLabelId());
								viewLable.setLabelName(labelList.get(j).getLableName());
								
								viewLableList.add(viewLable);
								
								flag2 = true;
								
							}
						}
						if(!flag2) {
							Label label = new Label();
							
							ViewLabelDto viewLable = new ViewLabelDto();
							
							label.setLableName(lableName);
							label.setUserId(userId);
							
							labelrepo.save(label);
							labelelasticrepo.save(label);
							
							viewLable.setLabelId(label.getLabelId());
							viewLable.setLabelName(label.getLableName());
							
							viewLableList.add(viewLable);
						}
					}
					else {
						Label label = new Label();
						ViewLabelDto viewLable = new ViewLabelDto();
						
						label.setLableName(lableName);
						label.setUserId(userId);
						
						labelrepo.save(label);
						labelelasticrepo.save(label);
						
						viewLable.setLabelId(label.getLabelId());
						viewLable.setLabelName(label.getLableName());
						
						viewLableList.add(viewLable);
					}
				}
			}
				
		}		
		note.setLabelList(viewLableList);
		
		if(isPinned) {
			note.setPinned(true);
		}
		if(isArchived) {
			note.setArchived(true);
		}
		
		if(!flag) {
		DateTime date = DateTime.parse(createDto.getReminder().getDay()+"/"+createDto.getReminder().getMonth()+"/"+createDto.getReminder().getYear()+" "+createDto.getReminder().getHours()+":"+createDto.getReminder().getMinutes()+":"+createDto.getReminder().getSeconds(),
				DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss"));
		
		
		
		note.setReminder(date.toDate());
		}
		else {
			note.setReminder(null);
		}
		
		
		notesrepo.save(note);
		notesElasticrepo.save(note);
		
		ViewNoteDto viewNote = new ViewNoteDto();
		
		modelMapper.map(viewNote, Note.class);
		
		/*viewNote.setBody(note.getBody());
		viewNote.setTitle(note.getTitle());
		viewNote.setCreatedAt(note.getCreatedAt());
		viewNote.setLastModified(note.getLastModified());
		viewNote.setReminder(note.getReminder());
		viewNote.setColor(note.getColor());
		viewNote.setLabelList(note.getLabelList());*/
		
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
		
		Optional<Note> note = notesElasticrepo.findById(noteId);
		
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
		notesElasticrepo.save(note.get());
		
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
		
		List<Note> note = notesElasticrepo.findAllByUserId(userId);
		List<ViewNoteDto> notesList = new ArrayList<>();
		if(note.isEmpty()) {
			throw new NoteNotFoundException("No Notes Found");
		}
		
		notesList=note.stream().filter(noteStream -> !noteStream.isTrashed())
				.map(filterNote -> modelMapper.map(filterNote, ViewNoteDto.class)).collect(Collectors.toList());
		
	
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
		
		Optional<Note> note = notesElasticrepo.findById(noteId);
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
		
		notesElasticrepo.save(note.get());
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
		
		Optional<Note> note = notesElasticrepo.findById(noteId);
		
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
		notesElasticrepo.delete(note.get());
	}
	
	/**
	 * To Set A Reminder
	 * 
	 * @param token
	 * @param noteId
	 * @throws UnauthorizedUserException
	 * @throws NoteNotFoundException
	 * @throws TokenParsingException
	 * @throws InvalidDateFormatException 
	 */
	@Override
	public void reminder(String userId, String noteId,DateDto dateDto) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException,InvalidDateFormatException {
		// TODO Auto-generated method stub
		//jwt.parseJWT(token);
		
		NotesUtil.ValidateDate(dateDto);
		Optional<Note> note = notesElasticrepo.findById(noteId);
		
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
		
		notesElasticrepo.save(note.get());
		
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
		
		Optional<Note> note = notesElasticrepo.findById(noteId);
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
		notesElasticrepo.save(note.get());

	}
	
	/**
	 * To Add Label
	 * 
	 * @param userId
	 * @param lableName
	 * @param noteId
	 * @throws LabelException
	 * @throws UnauthorizedUserException
	 * @throws NoteNotFoundException
	 * @throws TokenParsingException
	 */
	@Override
	public void addLable(String userId, String noteId, String labelName) throws TokenParsingException, NoteNotFoundException, LabelException, UnauthorizedUserException {
		
		boolean flag = false;
		
		Optional<Note> note = notesElasticrepo.findById(noteId);
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes found");
		}
		if(!note.get().getUserId().equals(userId)) {
			throw new UnauthorizedUserException("UnAuthorized User");
		}
		
		List<Label> labelList = new ArrayList<>();
		labelList = labelelasticrepo.findAllByUserId(userId);
		System.out.println(labelList.size());
		
		if(labelList.isEmpty()) {
			throw new LabelException("No Labels");
		}
		
		List<ViewLabelDto> tempList = note.get().getLabelList();
		System.out.println(tempList.size());
		
		for(int i =0;i<labelList.size();i++) {
			if(labelList.get(i).getLableName().equals(labelName)) {
				for(int j =0;j<tempList.size();j++) {
				
					if(tempList.get(j).getLabelName().equals(labelName)) {
						throw new LabelException("Label Already added");
					}
					
					ViewLabelDto label = new ViewLabelDto();
					label.setLabelName(labelList.get(i).getLableName());
					label.setLabelId(labelList.get(i).getLabelId());
					tempList.add(label);
					flag = true;
				}
			}
			
		}
		
		if(!flag) {
			Label label = new Label();
			label.setUserId(userId);
			label.setLableName(labelName);
			labelrepo.save(label);
			labelelasticrepo.save(label);
			
			ViewLabelDto viewLabel = new ViewLabelDto();
			viewLabel.setLabelName(label.getLableName());
			viewLabel.setLabelId(label.getLabelId());
			tempList.add(viewLabel);
		}
		note.get().setLabelList(tempList);
		
		notesrepo.save(note.get());
		notesElasticrepo.save(note.get());
		
	}
	/**
	 * To Remove Label
	 * 
	 * @param userId
	 * @param noteId
	 * @param lableName
	 * @throws LabelException
	 * @throws UnauthorizedUserException
	 * @throws NoteNotFoundException
	 * @throws TokenParsingException
	 */
	@Override
	public void removeLabel(String userId, String noteId, String labelName) throws TokenParsingException, NoteNotFoundException, LabelException, UnauthorizedUserException {
		// TODO Auto-generated method stub
		//jwt.parseJWT(token);
		boolean flag= false;
		Optional<Note> note = notesElasticrepo.findById(noteId);
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes found");
		}
		
		if(!note.get().getUserId().equals(userId)) {
			throw new UnauthorizedUserException("UnAuthorized User");
		}
		List<ViewLabelDto> tempList = new ArrayList<>() ;
		
		tempList =  note.get().getLabelList();
		
		if(tempList.isEmpty()) {
			throw new LabelException("No Labels");
		}
		
		for(int i =0;i<tempList.size();i++) {
			if(tempList.get(i).getLabelName().equals(labelName)) {
				tempList.remove(i);
				flag=true;
			}
		}
		if(!flag) {
			throw new LabelException("No Such Labels");
		}
		note.get().setLabelList(tempList);
		
		notesrepo.save(note.get());
		notesElasticrepo.save(note.get());
	}
	/**
	 * To Archive/Unarchive
	 * 
	 * @param userId
	 * @param noteId
	 * @param condition
	 * @throws UnauthorizedUserException
	 * @throws NoteNotFoundException
	 * @throws TokenParsingException
	 */
	@Override
	public void archive(String userId, String noteId, boolean condition) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException {
		// TODO Auto-generated method stub
		
		//jwt.parseJWT(token);
		
		Optional<Note> note = notesElasticrepo.findById(noteId);
		
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes Present");
		}
		if(!note.get().getUserId().equals(userId)) {
			throw new UnauthorizedUserException("UnAuthorized User");
		}
		
		note.get().setArchived(condition);
		notesrepo.save(note.get());
		notesElasticrepo.save(note.get());
		
	}
	/**
	 * To Pin/UnPin
	 * 
	 * @param userId
	 * @param noteId
	 * @param condition
	 * @throws UnauthorizedUserException
	 * @throws NoteNotFoundException
	 * @throws TokenParsingException
	 */
	@Override
	public void pin(String userId, String noteId, boolean condition) throws TokenParsingException, NoteNotFoundException, UnauthorizedUserException {
		// TODO Auto-generated method stub
		//jwt.parseJWT(token);
		
		Optional<Note> note = notesElasticrepo.findById(noteId);
		
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes Present");
		}
		if(!note.get().getUserId().equals(userId)) {
			throw new UnauthorizedUserException("UnAuthorized User");
		}
		
		note.get().setPinned(condition);
		
		notesrepo.save(note.get());
		notesElasticrepo.save(note.get());
	}
	
	/**
	 * To View All Labeled Notes
	 * 
	 * @param userId
	 * @param labelId
	 * @throws LabelException
	 * @throws NoteNotFoundException
	 */
	@Override
	public List<ViewNoteDto> viewLabeledNotes(String userId, String labelId) throws LabelException, NoteNotFoundException {
		// TODO Auto-generated method stub
		
		Optional<Label> label = labelelasticrepo.findById(labelId);
		List<Note> noteList = notesElasticrepo.findAllByUserId(userId);
		
		if(!label.isPresent()) {
			throw new LabelException("No Labels");
		}
		
		if(noteList.isEmpty()) {
			throw new NoteNotFoundException("No Notes");
		}
		
		List<ViewNoteDto> viewnoteList = new ArrayList<>();
		
		for(int i=0;i<noteList.size();i++) {
			List<ViewLabelDto> labelList = noteList.get(i).getLabelList();
			if(!labelList.isEmpty()) {
				for(int j=0;j<labelList.size();j++) {
					if(labelList.get(j).getLabelId().equals(labelId)) {
						ViewNoteDto viewNote = new ViewNoteDto();
						viewNote.setTitle(noteList.get(i).getTitle());
						viewNote.setBody(noteList.get(i).getBody());
						viewNote.setCreatedAt(noteList.get(i).getCreatedAt());
						viewNote.setLastModified(noteList.get(i).getLastModified());
						viewNote.setReminder(noteList.get(i).getReminder());
						
						viewnoteList.add(viewNote);
					}
				}
			}
		}
		if(viewnoteList.isEmpty()) {
			throw new LabelException("No Labels");
		}
		
		return viewnoteList;
		
	}
	/**
	 * To Add Color To A Note
	 * 
	 * @param userId
	 * @param color
	 * @param noteId
	 * @throws UnauthorizedUserException
	 * @throws NoteNotFoundException
	 */
	@Override
	public void addColor(String userId, String color,String noteId) throws NoteNotFoundException, UnauthorizedUserException {
		// TODO Auto-generated method stub
		Optional<Note> note = notesElasticrepo.findById(noteId);
		
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes");
		}
		if(!note.get().getUserId().equals(userId)) {
			throw new UnauthorizedUserException("Unauthorized user");
		}
		
		note.get().setColor(color);
		
		notesrepo.save(note.get());
		notesrepo.save(note.get());
		
	}
	/**
	 * To Remove Color From A Note
	 * 
	 * @param userId
	 * @param noteId
	 * @throws UnauthorizedUserException
	 * @throws NoteNotFoundException
	 */
	@Override
	public void removeColor(String userId, String noteId) throws NoteNotFoundException, UnauthorizedUserException {
		// TODO Auto-generated method stub
		Optional<Note> note = notesrepo.findById(noteId);
		
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes");
		}
		if(!note.get().getUserId().equals(userId)) {
			throw new UnauthorizedUserException("Unauthorized user");
		}
		note.get().setColor("white");
		
		notesrepo.save(note.get());
	}
	/**
	 * To View Pinned Notes
	 * 
	 * @param userId
	 * @throws UnauthorizedUserException
	 * @throws NoteNotFoundException
	 */
	@Override
	public List<ViewNoteDto> viewPinnedNotes(String userId) throws NoteNotFoundException {
		// TODO Auto-generated method stub
		List<Note> noteList = notesElasticrepo.findAllByUserId(userId);
		
		if(!noteList.isEmpty()) {
			throw new NoteNotFoundException("No Notes");
		}
		
		
		List<ViewNoteDto> viewnoteList = new ArrayList<>();
		List<ViewNoteDto> viewnoteListUnpinned = new ArrayList<>();
		
		viewnoteList=noteList.stream().filter(noteStream -> noteStream.isPinned())
		.map(filterNote -> modelMapper.map(filterNote, ViewNoteDto.class)).collect(Collectors.toList());
		
		
		viewnoteListUnpinned=noteList.stream().filter(noteStream -> !noteStream.isPinned())
				.map(filterNote -> modelMapper.map(filterNote, ViewNoteDto.class)).collect(Collectors.toList());

		
		viewnoteList.addAll(viewnoteListUnpinned);
		
		return viewnoteList;
	}
	/**
	 * To View Archived Notes
	 * 
	 * @param userId
	 * @throws UnauthorizedUserException
	 * @throws NoteNotFoundException
	 */
	@Override
	public List<ViewNoteDto> viewArchivedNotes(String userId) throws NoteNotFoundException {
		// TODO Auto-generated method stub
		List<Note> noteList = notesElasticrepo.findAllByUserId(userId);
		
		if(!noteList.isEmpty()) {
			throw new NoteNotFoundException("No Notes");
		}
		
		List<ViewNoteDto> viewnoteList = new ArrayList<>();
		
		viewnoteList = noteList.stream().filter(noteStream->noteStream.isArchived()).map(filterStream -> modelMapper.map(filterStream, ViewNoteDto.class)).collect(Collectors.toList());
		
		return viewnoteList;
	}

	@Override
	public ViewNoteDto addLink(String userId,String noteId,String link) throws NoteNotFoundException, UnauthorizedUserException, MalformedLinkException {
		
		Optional<Note> note = notesElasticrepo.findById(noteId);
		if(!note.isPresent()) {
			throw new NoteNotFoundException("No Notes found");
		}
		if(!note.get().getUserId().equals(userId)) {
			throw new UnauthorizedUserException("UnAuthorized User");
		}
		
		Document doc;
		try {
			doc = Jsoup.connect(link).get();
		} catch (IOException e) {
			throw new MalformedLinkException("MalformedLink");
		}  
		
		String description = doc.select("meta[name=description]").get(0).attr("content");  
		String imageUrl =  doc.select("img[src~=(?i)\\.(png|jpe?g|gif)]").attr("src"); 
		
		URLMetadata urlData = new URLMetadata();
		urlData.setUrl(link);
		urlData.setDescription(description);
		urlData.setImageUrl(imageUrl);
		
		List<URLMetadata> tempList = note.get().getUrlData();
		
		tempList.add(urlData);
		
		note.get().setUrlData(tempList);
		
		notesrepo.save(note.get());
		notesElasticrepo.save(note.get());
		
		return modelMapper.map(note.get(),ViewNoteDto.class);
	}
}
