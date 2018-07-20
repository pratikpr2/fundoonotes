package com.bridgelabz.fundoonotes.notes.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bridgelabz.fundoonotes.notes.model.Note;

public interface NotesRepository extends MongoRepository<Note, String>{
	
	public Optional<Note> findByUserId(String id);
	
	public List<Note> findAllByUserId(String id);
}
