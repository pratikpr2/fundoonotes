package com.bridgelabz.fundoonotes.notes.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.notes.exceptions.LabelException;
import com.bridgelabz.fundoonotes.notes.exceptions.LabelNotFoundException;
import com.bridgelabz.fundoonotes.notes.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonotes.notes.model.EditLabelDto;
import com.bridgelabz.fundoonotes.notes.model.NoteResponseDto;
import com.bridgelabz.fundoonotes.notes.model.ViewLabelDto;
import com.bridgelabz.fundoonotes.notes.services.NotesService;
import com.bridgelabz.fundoonotes.user.exception.TokenParsingException;

@RestController
@RequestMapping("/api/labels")
public class LabelController {
	
	@Autowired
	private NotesService notesService;
	
	
//---------------------Create Label---------------------------
	/**
	 * 
	 * @param req
	 * @param labelName
	 * @return
	 * @throws TokenParsingException
	 * @throws LabelException
	 */
	
	@RequestMapping(value="/createlabel",method=RequestMethod.POST)
	public ResponseEntity<ViewLabelDto> createLabel(HttpServletRequest req,@RequestBody String labelName) throws TokenParsingException, LabelException{
		
		String userId = (String)req.getAttribute("userId");
		
		ViewLabelDto viewLabel= notesService.createLable(userId, labelName);

		return new ResponseEntity<>(viewLabel,HttpStatus.OK);
	}
//---------------------View Label--------------------------------
	/**
	 * 
	 * @param req
	 * @return
	 * @throws LabelException
	 */
	@RequestMapping(value="/ViewAllLabels",method= RequestMethod.GET)
	public ResponseEntity<List<ViewLabelDto>> viewAllLabels(HttpServletRequest req) throws LabelException{
		
		String userId = (String)req.getAttribute("userId");
		
		List<ViewLabelDto> labelList=notesService.viewAllLabels(userId);
		
		return new ResponseEntity<>(labelList,HttpStatus.OK);
		
	}
//-----------------------Delete Label----------------------------
	/**
	 * 
	 * @param req
	 * @param labelId
	 * @return
	 * @throws TokenParsingException
	 * 
	 * 
	 * @throws LabelException
	 * @throws NoteNotFoundException
	 */
	@RequestMapping(value="/deletelabel",method= RequestMethod.DELETE)
	public ResponseEntity<NoteResponseDto> deleteLable(HttpServletRequest req, @RequestParam(value="labelId") String labelId) throws TokenParsingException, LabelException, NoteNotFoundException{
		
		String userId = (String)req.getAttribute("userId");
		
		notesService.deleteLable(userId,labelId);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Label Deleted");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

	//-----------------------Edit Label----------------------------
	/**
	 * 
	 * @param req
	 * @param labelId
	 * @return
	 * @throws TokenParsingException
	 * 
	 * 
	 * @throws LabelException
	 * @throws NoteNotFoundException
	 * @throws LabelNotFoundException 
	 */
	@RequestMapping(value="/editlabel",method= RequestMethod.DELETE)
	public ResponseEntity<NoteResponseDto> editLable(HttpServletRequest req, @RequestParam(value="labelId") String labelId,@RequestBody EditLabelDto editLableDto) throws TokenParsingException, LabelException, NoteNotFoundException, LabelNotFoundException{
		
		String userId = (String)req.getAttribute("userId");
		
		notesService.editLabel(userId,labelId,editLableDto);
		
		NoteResponseDto response = new NoteResponseDto();
		response.setMessage("Label Updated");
		response.setStatus(1);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
}
