package com.bridgelabz.fundoonotes.user.services;

import java.util.Optional;

import javax.mail.MessagingException;
import javax.security.auth.login.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.notes.repositories.TokenRepository;
import com.bridgelabz.fundoonotes.user.exception.ActivationException;
import com.bridgelabz.fundoonotes.user.exception.ChangePassException;
import com.bridgelabz.fundoonotes.user.exception.MalformedUUIDException;
import com.bridgelabz.fundoonotes.user.exception.RegistrationException;
import com.bridgelabz.fundoonotes.user.exception.TokenParsingException;
import com.bridgelabz.fundoonotes.user.model.ChangePassDTO;
import com.bridgelabz.fundoonotes.user.model.LoginDTO;
import com.bridgelabz.fundoonotes.user.model.MailDTO;
import com.bridgelabz.fundoonotes.user.model.MailUser;
import com.bridgelabz.fundoonotes.user.model.RegistrationDTO;
import com.bridgelabz.fundoonotes.user.model.User;
import com.bridgelabz.fundoonotes.user.rabbitmq.ProducerService;
import com.bridgelabz.fundoonotes.user.repository.UserElasticRepository;
import com.bridgelabz.fundoonotes.user.repository.UserRepository;
import com.bridgelabz.fundoonotes.user.token.JwtToken;
import com.bridgelabz.fundoonotes.user.utility.Utility;

@Service
public class UserServiceImpl implements UserService {

	@Value("${loginLink}")
	public String link;
	
	@Value("${activationLink}")
	public String activatelink;
	
	@Value("${resetPasswordLink}")
	public String resetPassLink;
	
	@Autowired 
	UserRepository mongoRepo;
	
	@Autowired
	UserMailService mailservice;
	
	@Autowired
	UserElasticRepository elasticRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	TokenRepository tokenRepo;
	
	@Autowired
	ProducerService producer;
	
	@Autowired
	JwtToken jwt;
	
	@Override
	public void login(LoginDTO logUser) throws LoginException {

		Utility.validateLoginUser(logUser);
		
		Optional<User> user = elasticRepo.findByUserEmail(logUser.getEmail());
		
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
		
		String loginLink = link+token;
		
		System.out.println(token);
		
		MailDTO mailuser = new MailDTO();
		mailuser.setEmail(user.get().getUserEmail());
		mailuser.setSubject("Login Validation");
		mailuser.setBody(loginLink);
		
		producer.send(mailuser);
	}

	@Override
	public void register(RegistrationDTO regUser) throws RegistrationException ,MessagingException{
		
		Utility.validateRegUser(regUser);
		
		Optional<User> checkuser = elasticRepo.findByUserEmail(regUser.getEmailId());
		
		if(checkuser.isPresent()) {
			throw new RegistrationException("User with Email "+ regUser.getEmailId()+" already Exists");
		}
		
		User user = new User();
		user.setUserName(regUser.getUserName());
		user.setUserEmail(regUser.getEmailId());
		user.setPhoneNumber(regUser.getPhoneNumber());
		user.setPassword(passwordEncoder.encode(regUser.getPassword()));
		
		mongoRepo.save(user);
		elasticRepo.save(user);
		
		String currentJwt = jwt.createJWT(user);
		
		String activationLink = activatelink+currentJwt;
		
		MailDTO mailuser = new MailDTO();
		mailuser.setEmail(user.getUserEmail());
		mailuser.setSubject("Activate Account");
		mailuser.setBody(activationLink);
		
		producer.send(mailuser);
		
	}

	@Override
	public void activateUser(String token) throws ActivationException, TokenParsingException {
		
		Optional<User> user =  elasticRepo.findById(jwt.getUserId(token));
		
		if(!user.isPresent()) {
			throw new ActivationException("Account Activation failed");
		}
		
		user.get().setStatus(true);
		
		mongoRepo.save(user.get());
		elasticRepo.save(user.get());
		
	}

	@Override
	public void changePassword(ChangePassDTO reset,String UUID) throws ChangePassException, MessagingException, ActivationException, TokenParsingException, MalformedUUIDException {
		
		Utility.validateChangePassDto(reset);
		Utility.validateUUID(UUID);
		
		String userId = tokenRepo.find(UUID);
		
		Optional<User> user = elasticRepo.findById(userId);
		
		if(!user.isPresent()) {
			throw new ActivationException("Invalid User");
		}
		
		user.get().setPassword(passwordEncoder.encode(reset.getPassword()));
		
		mongoRepo.save(user.get());
		elasticRepo.save(user.get());
		tokenRepo.delete(UUID);
	}

	@Override
	public void forgetPassword(MailUser mail) throws MessagingException, ChangePassException {
		
		Optional<User> checkUser = elasticRepo.findByUserEmail(mail.getEmail());

		if(!checkUser.isPresent()) {
			throw new ChangePassException("User Is Not Registered");
		}
		
		String UUID = Utility.generate();
		String mailBody = resetPassLink +UUID;
		
		MailDTO usermail = new MailDTO();
		usermail.setEmail(mail.getEmail());
		usermail.setSubject("Password Reset");
		usermail.setBody(mailBody);
		
		tokenRepo.save(UUID,checkUser.get().getUserId());
		producer.send(usermail);
	
	}
	

}
