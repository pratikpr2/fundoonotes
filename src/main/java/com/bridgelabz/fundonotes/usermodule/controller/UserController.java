package com.bridgelabz.fundonotes.usermodule.controller;

import javax.mail.MessagingException;
import javax.security.auth.login.LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundonotes.usermodule.exception.RegistrationException;
import com.bridgelabz.fundonotes.usermodule.model.LoginDTO;
import com.bridgelabz.fundonotes.usermodule.model.RegistrationDTO;
import com.bridgelabz.fundonotes.usermodule.model.ResponseDto;
import com.bridgelabz.fundonotes.usermodule.services.UserService;

@RestController
@RequestMapping("/Fundonotes")
public class UserController {

	public static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;
	
	//-------------------Login--------------------------
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ResponseEntity<ResponseDto> login(@RequestBody LoginDTO checkUser) throws LoginException {
	
		
		userService.login(checkUser);
		
		ResponseDto response = new ResponseDto();
		 response.setMessage("SuccessFully LoggedIn");
		 response.setStatus(1);
		return new ResponseEntity<ResponseDto>(response,HttpStatus.OK);
		
	}
	
	//----------------Register---------------------------
	
	
	@RequestMapping(value="/register",method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> register(@RequestBody RegistrationDTO regUser) throws RegistrationException, MessagingException{
		
		
		userService.register(regUser);
		
		logger.info("User registered with : {}",regUser.getEmailId());
		ResponseDto response = new ResponseDto();
		response.setMessage("SuccessFully Registered");
		response.setStatus(1);
		return new ResponseEntity<ResponseDto>(response,HttpStatus.OK);
	}
	//-------------------Activate Account-------------------
	
	@RequestMapping(value="/activateaccount",method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> activateaccount(@RequestParam(value="token")String token) throws RegistrationException {
		//System.out.println(hsr.getQueryString());
		//String token = hsr.getQueryString();
		ResponseDto response = new ResponseDto();
		if (userService.activateUser(token)) {
			response.setMessage("Account Activated SuccesFully");
			response.setStatus(1);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			response.setMessage("Failed To Activate Account");
			response.setStatus(0);
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}
}
	
}
