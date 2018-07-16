package com.bridgelabz.fundonotes.usermodule.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundonotes.usermodule.model.MailDTO;
import com.bridgelabz.fundonotes.usermodule.model.User;

@Component
public class UserMailServiceImpl implements UserMailService {

	@Autowired
	private JavaMailSender mailSender;
	
	
	@Override
	public void sendMail(String mail, User user) throws MessagingException {
		// TODO Auto-generated method stub
		System.out.println("into mail");
		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg);
		String to = user.getUserEmail();
		String subject = "Account Activation";
		String text = mail;
		//SimpleMailMessage msg = new SimpleMailMessage();
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text);
		mailSender.send(msg);
		
	}


	@Override
	public void sendMailv2(String mailBody, MailDTO user) throws MessagingException {
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

