package com.bridgelabz.fundoonotes.notes.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bridgelabz.fundoonotes.notes.model.Label;

public interface LabelRepository extends MongoRepository<Label,String>{

	public List<Label> findAllByUserId(String id);
	public Optional<Label> findByLableName(String lableName);
}
