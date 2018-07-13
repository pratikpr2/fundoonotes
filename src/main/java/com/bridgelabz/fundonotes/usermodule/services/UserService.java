package com.bridgelabz.fundonotes.usermodule.services;

import javax.security.auth.login.LoginException;

import com.bridgelabz.fundonotes.usermodule.exception.RegistrationException;
import com.bridgelabz.fundonotes.usermodule.model.LoginDTO;
import com.bridgelabz.fundonotes.usermodule.model.RegistrationDTO;

public interface UserService {

	public void login(LoginDTO logUser) throws LoginException;
	public void register(RegistrationDTO regUser) throws RegistrationException;
	public boolean activateUser(String token) throws RegistrationException;
}
