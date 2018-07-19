package com.bridgelabz.fundoonotes.user.controller;

import javax.mail.MessagingException;
import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.user.exception.ActivationException;
import com.bridgelabz.fundoonotes.user.exception.ChangePassException;
import com.bridgelabz.fundoonotes.user.exception.RegistrationException;
import com.bridgelabz.fundoonotes.user.model.ChangePassDTO;
import com.bridgelabz.fundoonotes.user.model.LoginDTO;
import com.bridgelabz.fundoonotes.user.model.MailUser;
import com.bridgelabz.fundoonotes.user.model.RegistrationDTO;
import com.bridgelabz.fundoonotes.user.model.ResponseDto;
import com.bridgelabz.fundoonotes.user.services.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService;

	// -------------------Login--------------------------

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> login(@RequestBody LoginDTO checkUser) throws LoginException {

		// System.out.println("Inside Login");
		userService.login(checkUser);

		ResponseDto response = new ResponseDto();
		response.setMessage("SuccessFully LoggedIn");
		response.setStatus(1);
		
		return new ResponseEntity<ResponseDto>(response, HttpStatus.OK);

		// generate token

	}

	// ----------------Register---------------------------

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> register(@RequestBody RegistrationDTO regUser)
			throws RegistrationException, MessagingException {

		userService.register(regUser);

		ResponseDto response = new ResponseDto();
		response.setMessage("SuccessFully Registered");
		response.setStatus(1);
		
		return new ResponseEntity<ResponseDto>(response, HttpStatus.OK);
	}
	// -------------------Activate Account-------------------

	@RequestMapping(value = "/activateaccount", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> activateaccount(@RequestParam(value = "token") String token)
			throws RegistrationException, ActivationException {
		// System.out.println(hsr.getQueryString());
		// String token = hsr.getQueryString();
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

	@RequestMapping(value = "/forgetpassword", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> forgetPassword(@RequestBody MailUser user)
			throws ChangePassException, MessagingException {

		userService.sendMail(user);
		ResponseDto response = new ResponseDto();
		response.setMessage("Please Check Mail To Confirm Changing Password");
		response.setStatus(2);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/resetpassword", method = RequestMethod.PUT)
	private ResponseEntity<ResponseDto> resetpassword(@RequestBody ChangePassDTO reset,
			@RequestParam(value = "token") String token)
			throws ChangePassException, MessagingException, ActivationException {

		ResponseDto response = new ResponseDto();

		if (!userService.activateUser(token)) {
			response.setMessage("Failed To Change Password");
			response.setStatus(3);
		}

		userService.changePassword(reset, token);

		response.setMessage("Password Changed SuccessFully");
		response.setStatus(3);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
