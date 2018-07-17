package com.bridgelabz.fundonotes.usermodule.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundonotes.usermodule.model.MailDTO;
import com.bridgelabz.fundonotes.usermodule.model.User;


@Component
public class ConsumerImpl implements Consumer{
	
	MailDTO mail;
	@Override
	@RabbitListener(queues="javainuse.queue")
	public void receive(MailDTO mail) {
		System.out.println("Receive msg = "+mail);
		this.mail = mail;
	}
	@Override
	public MailDTO receiveObject() {
		// TODO Auto-generated method stub
		return this.mail;
	}
	
		
}
