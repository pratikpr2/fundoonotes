package com.bridgelabz.fundoonotes.notes.repositories;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.bridgelabz.fundoonotes.notes.model.Label;

public interface LabelsElasticrepository extends ElasticsearchRepository<Label, String> {
	
	List<Label> findAllByUserId(String userId);
	
}
