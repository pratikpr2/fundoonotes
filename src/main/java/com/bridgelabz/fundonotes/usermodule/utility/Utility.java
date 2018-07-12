package com.bridgelabz.fundonotes.usermodule.utility;

import javax.security.auth.login.LoginException;

import com.bridgelabz.fundonotes.usermodule.exception.RegistrationException;
import com.bridgelabz.fundonotes.usermodule.model.LoginDTO;
import com.bridgelabz.fundonotes.usermodule.model.RegistrationDTO;

public class Utility {

	private static final String CONTACT_PATTERN = "^[0-9]{10}$";
	//private static final String KEY="todoapp";
	
	public static boolean validateLoginUser(RegistrationDTO registrationDto) throws RegistrationException{
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
			throw new RegistrationException("Didn't Match With Password");
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
}
