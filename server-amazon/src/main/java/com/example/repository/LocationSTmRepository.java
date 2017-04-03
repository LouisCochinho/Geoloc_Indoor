package com.example.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.domain.LocationSTm;

public interface LocationSTmRepository extends CrudRepository<LocationSTm, Integer> {
	LocationSTm findByidSTm(String idSTm);
	void delete(LocationSTm location);
}
