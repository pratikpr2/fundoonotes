package com.bridgelabz.fundoonotes.user.services;

import javax.mail.MessagingException;
import javax.security.auth.login.LoginException;

import com.bridgelabz.fundoonotes.user.exception.ActivationException;
import com.bridgelabz.fundoonotes.user.exception.ChangePassException;
import com.bridgelabz.fundoonotes.user.exception.RegistrationException;
import com.bridgelabz.fundoonotes.user.model.ChangePassDTO;
import com.bridgelabz.fundoonotes.user.model.LoginDTO;
import com.bridgelabz.fundoonotes.user.model.MailUser;
import com.bridgelabz.fundoonotes.user.model.RegistrationDTO;

public interface UserService {

	public void login(LoginDTO logUser) throws LoginException;

	public void register(RegistrationDTO regUser) throws RegistrationException, MessagingException;

	public boolean activateUser(String token) throws ActivationException;

	public void changePassword(ChangePassDTO reset, String token)
			throws ChangePassException, MessagingException, ActivationException;

	public void sendMail(MailUser mail) throws MessagingException, ChangePassException;

}
