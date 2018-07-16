package com.bridgelabz.fundonotes.usermodule.services;

import javax.mail.MessagingException;

import org.springframework.stereotype.Service;

import com.bridgelabz.fundonotes.usermodule.model.MailDTO;
import com.bridgelabz.fundonotes.usermodule.model.User;

@Service
public interface UserMailService {

	public void sendMail(String mailBody,User user) throws MessagingException;

	public void sendMailv2(String mailBody,MailDTO user) throws MessagingException;
}
