package com.bridgelabz.fundoonotes.notes.repositories;


public interface TokenRepository {

	public void save(String UUID,String userId);
	public String find(String UUID);
	public void delete(String UUID);
}
