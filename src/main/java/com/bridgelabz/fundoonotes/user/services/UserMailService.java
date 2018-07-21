package com.bridgelabz.fundoonotes.user.services;

import javax.mail.MessagingException;

import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.user.model.MailDTO;

@Service
public interface UserMailService {

	public void sendMailv2(MailDTO user) throws MessagingException;
}
