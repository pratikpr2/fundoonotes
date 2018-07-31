package com.bridgelabz.fundoonotes.user.rabbitmq;

import javax.mail.MessagingException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.user.model.MailDTO;
import com.bridgelabz.fundoonotes.user.services.UserMailService;

@Service
public class ConsumerServiceImpl implements ConsumerService{
	
	@Autowired
	UserMailService usermail;
	
	@Override
	@RabbitListener(queues="javainuse.queue")
	public void receive(MailDTO mail) throws MessagingException {
		System.out.println("Receive msg = "+mail);
		usermail.sendMailv2(mail);
	}
		
}
