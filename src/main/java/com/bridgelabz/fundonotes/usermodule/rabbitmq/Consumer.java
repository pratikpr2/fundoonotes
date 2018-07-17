package com.bridgelabz.fundonotes.usermodule.rabbitmq;

import com.bridgelabz.fundonotes.usermodule.model.MailDTO;

public interface Consumer {

	public void receive(MailDTO mail) ;
	public MailDTO receiveObject();
	
}
