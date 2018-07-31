package com.bridgelabz.fundoonotes.notes.repositories;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.bridgelabz.fundoonotes.notes.model.Note;

public interface NotesElasticRepository extends ElasticsearchRepository<Note, String>{
	
	List<Note> findAllByUserId(String userId);
	
}
