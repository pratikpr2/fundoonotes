package com.bridgelabz.fundoonotes.notes.repositories;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TokenRepositoryImpl implements TokenRepository {
	
	@Value("${mykey}")
	private String KEY;
	
	
	private RedisTemplate<String, String> redisTemplate;
	
	private HashOperations<String,String, String> hashOperation;
	
	@Autowired
	public TokenRepositoryImpl(RedisTemplate<String, String> redistemp) {
				this.redisTemplate=redistemp;
				hashOperation = this.redisTemplate.opsForHash();
	}
	
	@Override
	public void save(String UUID, String userId) {
		hashOperation.put(KEY, UUID, userId);
	}

	@Override
	public String find(String UUID) {
		// TODO Auto-generated method stub
		return hashOperation.get(KEY, UUID);
	}

	@Override
	public void delete(String UUID) {
		// TODO Auto-generated method stub
		hashOperation.delete(KEY, UUID);
	}

	
	
}
