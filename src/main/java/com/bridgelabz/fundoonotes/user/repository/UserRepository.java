package com.bridgelabz.fundoonotes.user.repository;

import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.user.model.User;

import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
	
}
