package com.bridgelabz.fundoonotes.user.controller;

import javax.mail.MessagingException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletResponse;

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
import com.bridgelabz.fundoonotes.user.exception.MalformedUUIDException;
import com.bridgelabz.fundoonotes.user.exception.RegistrationException;
import com.bridgelabz.fundoonotes.user.exception.TokenParsingException;
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

	/**
	 * @param checkUser
	 * @param response
	 * @return Login Response
	 * @throws LoginException
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> login(@RequestBody LoginDTO loginDto,HttpServletResponse res) throws LoginException {

		userService.login(loginDto);
		
		ResponseDto response = new ResponseDto();
		response.setMessage("SuccessFully LoggedIn");
		response.setStatus(1);
		
		return new ResponseEntity<ResponseDto>(response, HttpStatus.OK);

	}

	// ----------------Register---------------------------

	/**
	 * @param regUser
	 * @return Registration Response
	 * @throws RegistrationException
	 * @throws MessagingException
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> register(@RequestBody RegistrationDTO regDto)
			throws RegistrationException, MessagingException {

		userService.register(regDto);

		ResponseDto response = new ResponseDto();
		response.setMessage("SuccessFully Registered");
		response.setStatus(1);
		
		return new ResponseEntity<ResponseDto>(response, HttpStatus.OK);
	}
	// -------------------Activate Account-------------------

	/**
	 * @param token
	 * @return Activation Response
	 * @throws RegistrationException
	 * @throws ActivationException
	 * @throws TokenParsingException
	 */
	@RequestMapping(value = "/activateaccount", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> activateaccount(@RequestParam(value = "token") String token)
			throws RegistrationException, ActivationException, TokenParsingException {
	
		userService.activateUser(token);
		
		ResponseDto response = new ResponseDto();
		response.setMessage("Account Activated SuccesFully");
		response.setStatus(1);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	//----------------------Forget PassWord----------------------

	/**
	 * @param user
	 * @return Mail Response
	 * @throws ChangePassException
	 * @throws MessagingException
	 */
	@RequestMapping(value = "/forgetpassword", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> forgetPassword(@RequestBody MailUser user)
			throws ChangePassException, MessagingException {

		userService.sendMail(user);
		
		ResponseDto response = new ResponseDto();
		response.setMessage("Please Check Mail To Confirm Changing Password");
		response.setStatus(2);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	//----------------------Reset PassWord------------------------

	/**
	 * @param reset
	 * @param token
	 * @return ResetPassword Response
	 * @throws ChangePassException
	 * @throws MessagingException
	 * @throws ActivationException
	 * @throws TokenParsingException
	 * @throws MalformedUUIDException
	 */
	@RequestMapping(value = "/resetpassword", method = RequestMethod.PUT)
	private ResponseEntity<ResponseDto> resetpassword(@RequestBody ChangePassDTO reset,
			@RequestParam(value = "token") String token)
			throws ChangePassException, MessagingException, ActivationException, TokenParsingException, MalformedUUIDException {

		userService.changePassword(reset, token);
		
		ResponseDto response = new ResponseDto();
		response.setMessage("Password Changed SuccessFully");
		response.setStatus(3);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
