package com.bridgelabz.fundonotes.usermodule.services;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.bridgelabz.fundonotes.usermodule.model.User;

@Service
public class UserMailServiceImpl implements UserMailService {

	@Override
	public void activateUser(String token, User user) {
		// TODO Auto-generated method stub
		String from ="simranbodra9619"; //Mail User Name
		String pass ="Simran@4"; //password

		String to = user.getUserEmail();
		System.out.println(to);
		String subject = "Account Activation";
		
		String body = "Click here to activate account:\n\n"
				+"http://114.79.180.62:8080/Fundonotes/activateaccount/token=?"+token;
		
		Properties props = System.getProperties();
		String host = "smtp.gmail.com";
		
		props.put("mail.smtp.starttls.enable", "true");

		props.put("mail.smtp.ssl.trust", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getInstance(props);
		MimeMessage message = new MimeMessage(session);

		try {
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setText(body);
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} 
		catch (Exception ae) {
			ae.printStackTrace();
		}
		
	}
}

