package com.bridgelabz.fundonotes.usermodule.utility;

import java.util.Optional;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.security.auth.login.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundonotes.usermodule.exception.ChangePassException;
import com.bridgelabz.fundonotes.usermodule.exception.MailException;
import com.bridgelabz.fundonotes.usermodule.exception.RegistrationException;
import com.bridgelabz.fundonotes.usermodule.model.ChangePassDTO;
import com.bridgelabz.fundonotes.usermodule.model.LoginDTO;
import com.bridgelabz.fundonotes.usermodule.model.MailDTO;
import com.bridgelabz.fundonotes.usermodule.model.RegistrationDTO;
import com.bridgelabz.fundonotes.usermodule.model.User;
import com.bridgelabz.fundonotes.usermodule.repository.UserRepository;

@Service
public class Utility {

	
	@Autowired 
	UserRepository mongoRepo;
	
	private static final String CONTACT_PATTERN = "^[0-9]{10}$";
	//private static final String KEY="todoapp";
	
	public static boolean validateRegUser(RegistrationDTO registrationDto) throws RegistrationException{
		boolean flag = false;
		if(registrationDto.getUserName()==null || registrationDto.getUserName().length()<3) {
			throw new RegistrationException("User Name should Have atleast 3 Characters");
		}
		else if(registrationDto.getPhoneNumber()==null || !registrationDto.getPhoneNumber().matches(CONTACT_PATTERN)) {
			throw new RegistrationException("Contact Number Should be 10 digits Number");
		}
		else if(registrationDto.getPassword()==null || registrationDto.getPassword().length() < 8) {
			throw new RegistrationException("Password Should be of atleast 8 Characters");
		}
		else if(registrationDto.getConfirmPassword()==null || !registrationDto.getConfirmPassword().equals(registrationDto.getPassword())) {
			throw new RegistrationException("Confirm Password Didn't Match With Password");
		}
		else {
			flag=true;
		}
		return flag;
	}
	
	public static void validateLoginUser(LoginDTO loginDto) throws LoginException {
		if(loginDto.getEmail()==null ) {
			throw new LoginException("Invalid Email");
		}
		else if(loginDto.getPassword()==null || loginDto.getPassword().length() < 8) {
			throw new LoginException("Invalid Password");
		}
	}
	
	public static void validateEmailDto(MailDTO maildto) throws MailException {
		if(maildto.getEmail()==null || maildto.getEmail().length() < 3) {
			throw new MailException("Invalid Email");
		}
		else if(maildto.getBody()==null) {
			throw new MailException("Mail Body Null");
		}
		else if(maildto.getSubject()==null) {
			throw new MailException("Mail Subject Null");
		}
	}
	public static void validateChangePassDto(ChangePassDTO changepass) throws ChangePassException {
		if(changepass.getPassword()==null || changepass.getPassword().length() <8) {
			throw new ChangePassException("Invalid Password");
		}
		else if(!changepass.getPassword().equals(changepass.getConfirmPassword())) {
			throw new ChangePassException("Confirm Password Didn't Match with Password");
		}
	}
	
	
}
