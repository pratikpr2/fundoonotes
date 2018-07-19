package com.bridgelabz.fundoonotes.user.utility;

import javax.security.auth.login.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.user.exception.ChangePassException;
import com.bridgelabz.fundoonotes.user.exception.MailException;
import com.bridgelabz.fundoonotes.user.exception.RegistrationException;
import com.bridgelabz.fundoonotes.user.model.ChangePassDTO;
import com.bridgelabz.fundoonotes.user.model.LoginDTO;
import com.bridgelabz.fundoonotes.user.model.MailDTO;
import com.bridgelabz.fundoonotes.user.model.RegistrationDTO;
import com.bridgelabz.fundoonotes.user.model.User;
import com.bridgelabz.fundoonotes.user.repository.UserRepository;
import com.bridgelabz.fundoonotes.user.token.JwtToken;

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
	
	public static String generateToken(User user) {
		JwtToken jwt = new JwtToken();
		String token = jwt.createJWT(user);
		return token;
	}
}
