package com.example.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.domain.Beacon;

public interface LocationRepository extends CrudRepository<Beacon, Long> {

	List<Beacon> findByDeviceId(double deviceId);
	void deleteAll();
	void delete(Beacon b);
	//void delete(long id);
	List<Beacon> findAll();
}