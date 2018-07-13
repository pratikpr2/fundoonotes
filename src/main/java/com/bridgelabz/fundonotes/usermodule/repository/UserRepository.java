package com.bridgelabz.fundonotes.usermodule.repository;

import org.springframework.stereotype.Repository;

import com.bridgelabz.fundonotes.usermodule.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
	
}
