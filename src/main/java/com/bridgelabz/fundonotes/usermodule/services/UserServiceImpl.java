package com.bridgelabz.fundonotes.usermodule.services;

import java.util.Optional;

import javax.mail.MessagingException;
import javax.security.auth.login.LoginException;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundonotes.usermodule.exception.RegistrationException;
import com.bridgelabz.fundonotes.usermodule.model.LoginDTO;
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
	
	@Override
	public void login(LoginDTO logUser) throws LoginException {
		// TODO Auto-generated method stub
		Utility.validateLoginUser(logUser);
		Optional<User> user = mongoRepo.findById(logUser.getEmail());
		if(!user.isPresent()) {
			throw new LoginException("User With Email "+logUser.getEmail()+" Not Registered");
			
		}
		if(!user.get().getPassword().equals(logUser.getPassword())) {
			throw new LoginException("Wrong Password  ");
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
		user.setPassword(regUser.getPassword());
		mongoRepo.save(user);
		
		JwtToken jwt = new JwtToken();
		String currentJwt = jwt.createJWT(user);
		
		mailservice.sendMail(currentJwt, user);
		//Utility.sendActivationLink(currentJwt, user);
	}

	@Override
	public boolean activateUser(String token) throws RegistrationException{
		// TODO Auto-generated method stub
		boolean flag=true;
		Claims claim = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary("PRATIK")).parseClaimsJws(token).getBody();
		Optional<User> user =  mongoRepo.findById(claim.getSubject());
		
		if(!user.isPresent()) {
			flag=false;
			throw new RegistrationException("Invalid User");
		}
		user.get().setStatus(true);
		mongoRepo.save(user.get());
		return flag;
	}
	

}
