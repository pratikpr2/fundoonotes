package com.bridgelabz.fundonotes.usermodule.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundonotes.usermodule.exception.RegistrationException;
import com.bridgelabz.fundonotes.usermodule.model.RegistrationDTO;
import com.bridgelabz.fundonotes.usermodule.model.User;
import com.bridgelabz.fundonotes.usermodule.repository.MongoRepository;
import com.bridgelabz.fundonotes.usermodule.utility.Utility;

@Service
public class UserServiceImpl implements UserService {

	@Autowired 
	MongoRepository mongoRepo;
	
	@Override
	public User login(String emailId, String password) {
		// TODO Auto-generated method stub
		Optional<User> user = mongoRepo.findById(emailId);
		if(user!=null) {
			if(user.get().getPassword().equals(password)) {
				User loginuser =new User();
				loginuser.setUserId(user.get().getUserId());
				loginuser.setUserName(user.get().getUserName());
				loginuser.setUserEmail(user.get().getUserEmail());
				loginuser.setPhoneNumber(user.get().getPhoneNumber());
				loginuser.setPassword(user.get().getPassword());
			
				return loginuser;
			}
			else
				return null;
		}
		else
			return null;
	}

	@Override
	public boolean register(RegistrationDTO regUser) throws RegistrationException{
		// TODO Auto-generated method stub
		boolean flag = Utility.validateLoginUser(regUser);
		Optional<User> checkuser = mongoRepo.findById(regUser.getEmailId());
		if(flag && !checkuser.isPresent()) {
			User user = new User();
			user.setUserId("2");
			user.setUserName(regUser.getUserName());
			user.setUserEmail(regUser.getEmailId());
			user.setPhoneNumber(regUser.getPhoneNumber());
			user.setPassword(regUser.getPassword());
			
			mongoRepo.save(user);
			
			return true;
		}
		else {
			return false;
}
	}
	

}
