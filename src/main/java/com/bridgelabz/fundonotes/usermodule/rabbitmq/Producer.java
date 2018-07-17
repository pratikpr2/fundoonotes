package com.bridgelabz.fundonotes.usermodule.rabbitmq;

import com.bridgelabz.fundonotes.usermodule.model.MailDTO;

public interface Producer {

	public void send(MailDTO mail);
	
}
