package com.bridgelabz.fundoonotes.user.services;

import java.util.Optional;

import javax.mail.MessagingException;
import javax.security.auth.login.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.user.exception.ActivationException;
import com.bridgelabz.fundoonotes.user.exception.ChangePassException;
import com.bridgelabz.fundoonotes.user.exception.RegistrationException;
import com.bridgelabz.fundoonotes.user.exception.TokenParsingException;
import com.bridgelabz.fundoonotes.user.model.ChangePassDTO;
import com.bridgelabz.fundoonotes.user.model.LoginDTO;
import com.bridgelabz.fundoonotes.user.model.MailDTO;
import com.bridgelabz.fundoonotes.user.model.MailUser;
import com.bridgelabz.fundoonotes.user.model.RegistrationDTO;
import com.bridgelabz.fundoonotes.user.model.User;
import com.bridgelabz.fundoonotes.user.rabbitmq.ProducerService;
import com.bridgelabz.fundoonotes.user.repository.UserRepository;
import com.bridgelabz.fundoonotes.user.token.JwtToken;
import com.bridgelabz.fundoonotes.user.utility.Utility;

@Service
public class UserServiceImpl implements UserService {

	@Autowired 
	UserRepository mongoRepo;
	
	@Autowired
	UserMailService mailservice;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	ProducerService producer;
	
	@Autowired
	JwtToken jwt;
	
	@Override
	public void login(LoginDTO logUser) throws LoginException {
		// TODO Auto-generated method stub
		Utility.validateLoginUser(logUser);
		Optional<User> user = mongoRepo.findByUserEmail(logUser.getEmail());
		
		if(!user.isPresent()) {
			throw new LoginException("User With Email "+logUser.getEmail()+" Not Registered");
		}
		else if(!user.get().isStatus()) {
			throw new LoginException("Please Activate Account");
			
		}
		else if(!passwordEncoder.matches(logUser.getPassword(), user.get().getPassword())) {
			throw new LoginException("Wrong Password");
		}
		
		String token =jwt.createJWT(user.get());
		String activationLink = "Your Login token:\n\n"
				+"http://192.168.0.71:8080/activateaccount/?token="+token;
		
		
		System.out.println(token);
		
		MailDTO mailuser = new MailDTO();
		mailuser.setEmail(user.get().getUserEmail());
		mailuser.setSubject("Activate Account");
		mailuser.setBody(activationLink);
		
		producer.send(mailuser);
	}

	@Override
	public void register(RegistrationDTO regUser) throws RegistrationException ,MessagingException{
		// TODO Auto-generated method stub
		Utility.validateRegUser(regUser);
		
		Optional<User> checkuser = mongoRepo.findByUserEmail(regUser.getEmailId());
		//System.out.println(checkuser.isPresent());
		
		if(checkuser.isPresent()) {
			throw new RegistrationException("User with Email "+ regUser.getEmailId()+" already Exists");
		}
		
		User user = new User();
		user.setUserName(regUser.getUserName());
		user.setUserEmail(regUser.getEmailId());
		user.setPhoneNumber(regUser.getPhoneNumber());
		user.setPassword(passwordEncoder.encode(regUser.getPassword()));
		mongoRepo.save(user);
		
		
		String currentJwt = jwt.createJWT(user);
		
		String activationLink = "Click here to activate account:\n\n"
				+"http://192.168.0.71:8080/activateaccount/?token="+currentJwt;
		
		MailDTO mailuser = new MailDTO();
		mailuser.setEmail(user.getUserEmail());
		mailuser.setSubject("Activate Account");
		mailuser.setBody(activationLink);
		
		producer.send(mailuser);
		
		//Utility.sendActivationLink(currentJwt, user);
	}

	@Override
	public void activateUser(String token) throws ActivationException, TokenParsingException {
		// TODO Auto-generated method stub
		Optional<User> user =  mongoRepo.findById(jwt.getUserId(token));
		
		if(!user.isPresent()) {
			throw new ActivationException("Account Activation failed");
		}
		
		user.get().setStatus(true);
		mongoRepo.save(user.get());
	
	}

	@Override
	public void changePassword(ChangePassDTO reset,String token) throws ChangePassException, MessagingException, ActivationException, TokenParsingException {
		// TODO Auto-generated method stub
		
		Utility.validateChangePassDto(reset);
	
		Optional<User> user = mongoRepo.findById(jwt.getUserId(token));
		
		if(!user.isPresent()) {
			throw new ActivationException("Invalid User");
		}
		
		user.get().setPassword(passwordEncoder.encode(reset.getPassword()));
		mongoRepo.save(user.get());
	}

	@Override
	public void sendMail(MailUser mail) throws MessagingException, ChangePassException {
		// TODO Auto-generated method stub
		
		Optional<User> checkUser = mongoRepo.findByUserEmail(mail.getEmail());

		if(!checkUser.isPresent()) {
			throw new ChangePassException("User Is Not Registered");
		}
		
		String currentJwt = Utility.generateToken(checkUser.get());
		
		String mailBody = "Click here to reset Password:\n\n"
				+"http://192.168.0.71:8080/resetpassword/?token="+currentJwt;
		
		MailDTO usermail = new MailDTO();
		usermail.setEmail(mail.getEmail());
		usermail.setSubject("Password Reset");
		usermail.setBody(mailBody);
		
		producer.send(usermail);
	
	}
	

}
