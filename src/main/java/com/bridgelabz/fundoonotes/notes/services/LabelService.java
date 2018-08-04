package com.bridgelabz.fundoonotes.notes.services;

import java.util.List;

import com.bridgelabz.fundoonotes.notes.exceptions.LabelException;
import com.bridgelabz.fundoonotes.notes.exceptions.LabelNotFoundException;
import com.bridgelabz.fundoonotes.notes.model.EditLabelDto;
import com.bridgelabz.fundoonotes.notes.model.ViewLabelDto;

public interface LabelService {

	public ViewLabelDto createLabel(String userId,String labelName) throws LabelException;
	public List<ViewLabelDto> viewAllLabels(String userId);
	public void deleteLabel(String userId,String labelId) throws LabelNotFoundException;
	public void editLabel(String userId,String labelId,EditLabelDto editLabelDto) throws LabelException, LabelNotFoundException;
}
