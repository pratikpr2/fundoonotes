package com.bridgelabz.fundonotes.usermodule.services;

import javax.mail.MessagingException;

import org.springframework.stereotype.Service;

import com.bridgelabz.fundonotes.usermodule.model.User;

@Service
public interface UserMailService {

	public void sendMail(String token,User user) throws MessagingException;
}
