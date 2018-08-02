package com.bridgelabz.fundoonotes.notes.repositories;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TokenRepositoryImpl implements TokenRepository {

	private static final String KEY = "PRATIK";
	
	
	private RedisTemplate<String, String> redisTemplate;
	private HashOperations<String,String, String> hashOperation;
	
	@Autowired
	public TokenRepositoryImpl(RedisTemplate<String, String> redistemp) {
		// TODO Auto-generated constructor stub
		this.redisTemplate=redistemp;
	}
	
	@PostConstruct
	public void init() {
		hashOperation = redisTemplate.opsForHash();
	}
	
	@Override
	public void save(String token, String userId) {
		// TODO Auto-generated method stub
		hashOperation.put(KEY, token, userId);
	}

	@Override
	public String find(String token) {
		// TODO Auto-generated method stub
		return hashOperation.get(KEY, token);
	}

}
