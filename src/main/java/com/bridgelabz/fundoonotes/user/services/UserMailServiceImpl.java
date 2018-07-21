package com.bridgelabz.fundoonotes.user.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoonotes.user.model.MailDTO;

@Component
public class UserMailServiceImpl implements UserMailService {

	@Autowired
	private JavaMailSender mailSender;


	@Override
	public void sendMailv2(MailDTO user) throws MessagingException {
		// TODO Auto-generated method stub
		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg);
		
		String to = user.getEmail();
		String subject = user.getSubject();
		String text = user.getBody();
		
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text);
		
		mailSender.send(msg);
	}
	
	
}

