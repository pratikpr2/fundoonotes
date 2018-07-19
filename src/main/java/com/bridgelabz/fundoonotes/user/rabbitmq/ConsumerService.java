package com.bridgelabz.fundoonotes.user.rabbitmq;

import javax.mail.MessagingException;

import com.bridgelabz.fundoonotes.user.model.MailDTO;

public interface ConsumerService {

	public void receive(MailDTO mail) throws MessagingException ;
	
}
