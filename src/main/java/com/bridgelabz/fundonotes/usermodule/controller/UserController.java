package com.bridgelabz.fundonotes.usermodule.controller;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	public ResponseEntity<String> login(@RequestBody LoginDTO checkUser){
		logger.info("Logging User : {}",checkUser);
		
		User user = userService.login(checkUser.getEmail(), checkUser.getPassword());
	
		if(user==null) {
			logger.error("User With email {} not found",checkUser.getEmail());
			 
			return new ResponseEntity<>(new LoginException("User with email"+checkUser.getEmail()+"not Found").toString(),HttpStatus.NOT_FOUND);
		
		}
		
		String message = "Hello "+ user.getUserName()+ " ID: "+ user.getUserId()+"Email: "+user.getUserEmail()+"Contact Number: "+ user.getPhoneNumber();
		
		return new ResponseEntity<String>(message,HttpStatus.OK);
		
	}
	
	//----------------Register---------------------------
	
	
	@RequestMapping(value="/register",method = RequestMethod.POST)
	public ResponseEntity<String> register(@RequestBody RegistrationDTO regUser) throws RegistrationException{
		
		logger.info("Regster User : {}" ,regUser);
		
		boolean registered = userService.register(regUser);
		
		if(!registered) {
			logger.error("User with email {} already present"+ regUser.getEmailId());
			return new ResponseEntity<>(new RegistrationException("User With email: "+regUser.getEmailId()+" already Exists").toString(),HttpStatus.CONFLICT);
		}
		
		logger.info("User registered with : {}",regUser.getEmailId());
		String message = " Successfully Registered";
		
		return new ResponseEntity<String>(message,HttpStatus.OK);
	}
}
