package com.bridgelabz.fundoonotes.user.rabbitmq;

import com.bridgelabz.fundoonotes.user.model.MailDTO;

public interface ProducerService {

	public void send(MailDTO mail);
	
}
