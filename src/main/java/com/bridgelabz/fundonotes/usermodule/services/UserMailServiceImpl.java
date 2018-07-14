package com.bridgelabz.fundonotes.usermodule.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.swing.text.html.MinimalHTMLWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import com.bridgelabz.fundonotes.usermodule.model.User;

@Component
public class UserMailServiceImpl implements UserMailService {

	@Autowired
	private JavaMailSender mailSender;
	
	
	@Override
	public void sendMail(String token, User user) throws MessagingException {
		// TODO Auto-generated method stub
		System.out.println("into mail");
		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg);
		String to = user.getUserEmail();
		String subject = "Account Activation";
		String text = "Click here to activate account:\n\n"
				+"http://192.168.0.71:8080/Fundonotes/activateaccount/?token="+token;
		//SimpleMailMessage msg = new SimpleMailMessage();
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text);
		mailSender.send(msg);
		
	}

	/*@Override
	public void sendMail(String token, User user) {
		// TODO Auto-generated method stub
		String from ="simranbodra9619"; //Mail User Name
		String pass ="Simran@4"; //password

		String to = user.getUserEmail();
		System.out.println(to);
		String subject = "Account Activation";
		
		String body = "Click here to activate account:\n\n"
				+"http://192.168.0.71/Fundonotes/activateaccount/token=?"+token;
		
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
		
	}*/
}

