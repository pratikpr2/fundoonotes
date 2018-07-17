package com.bridgelabz.fundonotes.usermodule.services;

import javax.mail.MessagingException;
import javax.security.auth.login.LoginException;

import com.bridgelabz.fundonotes.usermodule.exception.ActivationException;
import com.bridgelabz.fundonotes.usermodule.exception.ChangePassException;
import com.bridgelabz.fundonotes.usermodule.exception.RegistrationException;
import com.bridgelabz.fundonotes.usermodule.model.ChangePassDTO;
import com.bridgelabz.fundonotes.usermodule.model.LoginDTO;
import com.bridgelabz.fundonotes.usermodule.model.MailUser;
import com.bridgelabz.fundonotes.usermodule.model.RegistrationDTO;

public interface UserService {

	public void login(LoginDTO logUser) throws LoginException;
	public void register(RegistrationDTO regUser) throws RegistrationException,  MessagingException;
	public boolean activateUser(String token) throws ActivationException;
	public void changePassword(ChangePassDTO reset, String token) throws ChangePassException, MessagingException, ActivationException;
	public void sendMail(MailUser mail) throws MessagingException, ChangePassException;

}
