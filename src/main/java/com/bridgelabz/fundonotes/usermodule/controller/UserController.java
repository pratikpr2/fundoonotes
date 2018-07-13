package com.bridgelabz.fundonotes.usermodule.controller;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

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
import com.bridgelabz.fundonotes.usermodule.model.User;
import com.bridgelabz.fundonotes.usermodule.services.UserService;

@RestController
@RequestMapping("/Fundonotes")
public class UserController {

	public static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;
	
	//-------------------Login--------------------------
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ResponseEntity<String> login(@RequestBody LoginDTO checkUser) throws LoginException {
	
		
		userService.login(checkUser);
		
		String message = "Hello "+checkUser.getEmail()+ " Login SuccessFul";
		return new ResponseEntity<String>(message,HttpStatus.OK);
		
	}
	
	//----------------Register---------------------------
	
	
	@RequestMapping(value="/register",method = RequestMethod.POST)
	public ResponseEntity<String> register(@RequestBody RegistrationDTO regUser) throws RegistrationException{
		
		
		userService.register(regUser);
		
		logger.info("User registered with : {}",regUser.getEmailId());
		String message = " Successfully Registered";
		
		return new ResponseEntity<String>(message,HttpStatus.OK);
	}
	@RequestMapping(value="/activateaccount",method = RequestMethod.GET)
	public ResponseEntity<String> activateaccount(@RequestParam(value="token")String token) throws RegistrationException {
		//System.out.println(hsr.getQueryString());
		//String token = hsr.getQueryString();

		if (userService.activateUser(token)) {
			String messege = "Account activated successfully";
			return new ResponseEntity<String>(messege, HttpStatus.OK);
		} else {
			String msg = "Account not activated";
			return new ResponseEntity<String>(msg, HttpStatus.FORBIDDEN);
		}
}
	
}
