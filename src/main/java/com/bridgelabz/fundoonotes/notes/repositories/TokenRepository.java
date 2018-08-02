package com.bridgelabz.fundoonotes.notes.repositories;


public interface TokenRepository {

	public void save(String token,String userId);
	public String find(String token);
	
}
