package com.bridgelabz.fundonotes.usermodule.services;

import org.springframework.stereotype.Service;

import com.bridgelabz.fundonotes.usermodule.model.User;

@Service
public interface UserMailService {

	public void activateUser(String token,User user);
}
