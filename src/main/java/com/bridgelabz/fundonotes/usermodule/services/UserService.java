package com.bridgelabz.fundonotes.usermodule.services;

import com.bridgelabz.fundonotes.usermodule.exception.RegistrationException;
import com.bridgelabz.fundonotes.usermodule.model.RegistrationDTO;
import com.bridgelabz.fundonotes.usermodule.model.User;

public interface UserService {

	public User login(String emailId, String password);
	public boolean register(RegistrationDTO regUser) throws RegistrationException;
	
}
