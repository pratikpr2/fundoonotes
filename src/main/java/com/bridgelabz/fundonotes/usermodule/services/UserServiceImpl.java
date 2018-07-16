package com.bridgelabz.fundonotes.usermodule.services;

import java.util.Optional;

import javax.mail.MessagingException;
import javax.security.auth.login.LoginException;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundonotes.usermodule.exception.ActivationException;
import com.bridgelabz.fundonotes.usermodule.exception.ChangePassException;
import com.bridgelabz.fundonotes.usermodule.exception.RegistrationException;
import com.bridgelabz.fundonotes.usermodule.model.ChangePassDTO;
import com.bridgelabz.fundonotes.usermodule.model.LoginDTO;
import com.bridgelabz.fundonotes.usermodule.model.MailDTO;
import com.bridgelabz.fundonotes.usermodule.model.MailUser;
import com.bridgelabz.fundonotes.usermodule.model.RegistrationDTO;
import com.bridgelabz.fundonotes.usermodule.model.User;
import com.bridgelabz.fundonotes.usermodule.repository.UserRepository;
import com.bridgelabz.fundonotes.usermodule.token.JwtToken;
import com.bridgelabz.fundonotes.usermodule.utility.Utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class UserServiceImpl implements UserService {

	@Autowired 
	UserRepository mongoRepo;
	
	@Autowired
	UserMailService mailservice;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public void login(LoginDTO logUser) throws LoginException {
		// TODO Auto-generated method stub
		Utility.validateLoginUser(logUser);
		Optional<User> user = mongoRepo.findById(logUser.getEmail());
		
		if(!user.isPresent()) {
			throw new LoginException("User With Email "+logUser.getEmail()+" Not Registered");
		}
		else if(!user.get().isStatus()) {
			throw new LoginException("Please Activate Account");
			
		}
		else if(!passwordEncoder.matches(logUser.getPassword(), user.get().getPassword())) {
			throw new LoginException("Wrong Password");
		}
		
	}

	@Override
	public void register(RegistrationDTO regUser) throws RegistrationException ,MessagingException{
		// TODO Auto-generated method stub
		Utility.validateRegUser(regUser);
		Optional<User> checkuser = mongoRepo.findById(regUser.getEmailId());
		System.out.println(checkuser.isPresent());
		if(checkuser.isPresent()) {
			throw new RegistrationException("User with Email "+ regUser.getEmailId()+" already Exists");
		}
		User user = new User();
		user.setUserName(regUser.getUserName());
		user.setUserEmail(regUser.getEmailId());
		user.setPhoneNumber(regUser.getPhoneNumber());
		user.setPassword(passwordEncoder.encode(regUser.getPassword()));
		mongoRepo.save(user);
		
		JwtToken jwt = new JwtToken();
		String currentJwt = jwt.createJWT(user);
		
		String activationLink = "Click here to activate account:\n\n"
				+"http://192.168.0.71:8080/Fundonotes/activateaccount/?token="+currentJwt;
		
		mailservice.sendMail(activationLink, user);
		//Utility.sendActivationLink(currentJwt, user);
	}

	@Override
	public boolean activateUser(String token) throws ActivationException {
		// TODO Auto-generated method stub
		boolean flag=true;
		Claims claim = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary("PRATIK")).parseClaimsJws(token).getBody();
		Optional<User> user =  mongoRepo.findById(claim.getSubject());
		
		if(!user.isPresent()) {
			flag=false;
			throw new ActivationException("Invalid User");
		}
		user.get().setStatus(true);
		mongoRepo.save(user.get());
		return flag;
	}

	@Override
	public void changePassword(ChangePassDTO reset,String token) throws ChangePassException, MessagingException, ActivationException {
		// TODO Auto-generated method stub
		
		Utility.validateChangePassDto(reset);
		Claims claim = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary("PRATIK")).parseClaimsJws(token).getBody();
		Optional<User> user = mongoRepo.findById(claim.getSubject());
		if(!user.isPresent()) {
			throw new ActivationException("Invalid User");
		}
		user.get().setPassword(passwordEncoder.encode(reset.getPassword()));
		mongoRepo.save(user.get());
	}

	@Override
	public void sendMail(MailUser mail) throws MessagingException, ChangePassException {
		// TODO Auto-generated method stub
		
		Optional<User> checkUser = mongoRepo.findById(mail.getEmail());

		if(!checkUser.isPresent()) {
			throw new ChangePassException("User Is Not Registered");
		}
		
		JwtToken jwt = new JwtToken();
		String currentJwt = jwt.createJWT(checkUser.get());
		
		String mailBody = "Click here to reset Password:\n\n"
				+"http://192.168.0.71:8080/Fundonotes/resetpassword/?token="+currentJwt;
		
		MailDTO usermail = new MailDTO();
		usermail.setEmail(mail.getEmail());
		usermail.setSubject("Password Reset");
		usermail.setBody(mailBody);
		mailservice.sendMailv2(mailBody, usermail);
	}
	

}
