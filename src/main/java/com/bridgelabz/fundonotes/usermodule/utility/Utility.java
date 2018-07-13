package com.bridgelabz.fundonotes.usermodule.utility;

import java.util.Optional;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.security.auth.login.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundonotes.usermodule.exception.RegistrationException;
import com.bridgelabz.fundonotes.usermodule.model.LoginDTO;
import com.bridgelabz.fundonotes.usermodule.model.RegistrationDTO;
import com.bridgelabz.fundonotes.usermodule.model.User;
import com.bridgelabz.fundonotes.usermodule.repository.UserRepository;

@Service
public class Utility {

	
	@Autowired 
	UserRepository mongoRepo;
	
	private static final String CONTACT_PATTERN = "^[0-9]{10}$";
	//private static final String KEY="todoapp";
	
	public static boolean validateRegUser(RegistrationDTO registrationDto) throws RegistrationException{
		boolean flag = false;
		if(registrationDto.getUserName()==null || registrationDto.getUserName().length()<3) {
			throw new RegistrationException("User Name should Have atleast 3 Characters");
		}
		else if(registrationDto.getPhoneNumber()==null || !registrationDto.getPhoneNumber().matches(CONTACT_PATTERN)) {
			throw new RegistrationException("Contact Number Should be 10 digits Number");
		}
		else if(registrationDto.getPassword()==null || registrationDto.getPassword().length() < 8) {
			throw new RegistrationException("Password Should be of atleast 8 Characters");
		}
		else if(registrationDto.getConfirmPassword()==null || !registrationDto.getConfirmPassword().equals(registrationDto.getPassword())) {
			throw new RegistrationException("Didn't Match With Password");
		}
		else {
			flag=true;
		}
		return flag;
	}
	
	public static void validateLoginUser(LoginDTO loginDto) throws LoginException {
		if(loginDto.getEmail()==null ) {
			throw new LoginException("Invalid Email");
		}
		else if(loginDto.getPassword()==null || loginDto.getPassword().length() < 8) {
			throw new LoginException("Invalid Password");
		}
	}
	
	public static void sendActivationLink(String jwToken,User user) {
		String from ="simranbodra9619"; //Mail User Name
		String pass ="Simran@4"; //password

		String to = user.getUserEmail();
		System.out.println(to);
		String subject = "Account Activation";
		
		String body = "Click here to ativate account:\n\n"
				+"http://114.79.180.62:8080/Fundonotes/activateaccount/token=?"+jwToken;
		
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
