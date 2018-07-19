package com.bridgelabz.fundoonotes.notes.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bridgelabz.fundoonotes.notes.model.Note;

public interface NotesRepository extends MongoRepository<Note, String>{
	
}
