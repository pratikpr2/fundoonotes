package com.bridgelabz.fundoonotes.user.repository;

import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.bridgelabz.fundoonotes.user.model.User;

public interface UserElasticRepository extends ElasticsearchRepository<User, String> {

	public  Optional<User> findByUserEmail(String email);
}
