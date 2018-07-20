package com.bridgelabz.fundoonotes.user.repository;

import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.user.model.User;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
	
	public Optional<User> findByUserEmail(String email);
	
}
