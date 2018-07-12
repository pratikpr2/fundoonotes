package com.bridgelabz.fundonotes.usermodule.repository;

import org.springframework.stereotype.Repository;

import com.bridgelabz.fundonotes.usermodule.model.User;

@Repository
public interface MongoRepository extends org.springframework.data.mongodb.repository.MongoRepository<User, String>{
	
}
