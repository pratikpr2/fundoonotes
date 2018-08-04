package com.bridgelabz.fundoonotes.notes.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.notes.exceptions.LabelException;
import com.bridgelabz.fundoonotes.notes.exceptions.LabelNotFoundException;
import com.bridgelabz.fundoonotes.notes.model.EditLabelDto;
import com.bridgelabz.fundoonotes.notes.model.Label;
import com.bridgelabz.fundoonotes.notes.model.Note;
import com.bridgelabz.fundoonotes.notes.model.ViewLabelDto;
import com.bridgelabz.fundoonotes.notes.repositories.LabelRepository;
import com.bridgelabz.fundoonotes.notes.repositories.LabelsElasticrepository;
import com.bridgelabz.fundoonotes.notes.repositories.NotesElasticRepository;
import com.bridgelabz.fundoonotes.notes.repositories.NotesRepository;

@Service
public class LabelServiceImpl implements LabelService {

	@Autowired
	LabelRepository labelrepo;
	
	@Autowired
	LabelsElasticrepository labelelasticrepo;
	
	@Autowired 
	NotesRepository notesrepo;
	
	@Autowired
	NotesElasticRepository notesElasticrepo;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	public ViewLabelDto createLabel(String userId, String labelName) throws LabelException {
		// TODO Auto-generated method stub
		if((labelName.trim().length()==0 || labelName.equals(null) || labelName.replaceAll("\"","").trim().length()==0)) {
			throw new LabelException("No Label Name");
		}
		
		List<Label> lableList = labelelasticrepo.findAllByUserId(userId);		
		
		for(int i=0;i<lableList.size();i++) {
			if(lableList.get(i).getLableName().equals(labelName)) {
				throw new LabelException("Label Already Exists");
			}
		}
		
		Label label = new Label();
		label.setUserId(userId);
		label.setLableName(labelName);
		
		labelrepo.save(label);
		labelelasticrepo.save(label);
		
		ViewLabelDto viewLabel =modelMapper.map(label, ViewLabelDto.class);
		
		return viewLabel;
	}

	@Override
	public List<ViewLabelDto> viewAllLabels(String userId) {
		
		List<Label> labelList = labelelasticrepo.findAllByUserId(userId);
		List<ViewLabelDto> viewLabelList = new ArrayList<>();
		
		if(viewLabelList!=null) {
		
			viewLabelList=labelList.stream().map(labelListStream -> modelMapper.map(labelListStream,ViewLabelDto.class)).collect(Collectors.toList());
		
		}
		return viewLabelList;
	}

	@Override
	public void deleteLabel(String userId, String labelId) throws LabelNotFoundException {
		// TODO Auto-generated method stub

		Optional<Label> label = labelelasticrepo.findById(labelId);
		if(!label.isPresent()) {
			throw new LabelNotFoundException("No Such Label");
		}
		List<Note> noteList = notesElasticrepo.findAllByUserId(userId);
				
		for(int i=0;i<noteList.size();i++) {
			List<ViewLabelDto> labelList = noteList.get(i).getLabelList();
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
			notesElasticrepo.save(noteList.get(i));
		}
		labelrepo.deleteById(labelId);
		labelelasticrepo.deleteById(labelId);
	
	}

	@Override
	public void editLabel(String userId, String labelId, EditLabelDto editLabelDto) throws LabelException, LabelNotFoundException {
		
		if(editLabelDto.getName().trim().length()==0) {
			throw new LabelException("No Label Name");
		}
		
		Optional<Label> label = labelrepo.findByLabelIdAndUserId(labelId, userId);
		
		if(!label.isPresent()) {
			throw new LabelNotFoundException("No Labels Present");
		}
		label.get().setLableName(editLabelDto.getName());
		
		labelrepo.save(label.get());
		labelelasticrepo.save(label.get());
	}

}
