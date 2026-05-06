package com.weather.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weather.demo.model.CityEntity;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {
	
	Optional<CityEntity> findByName(String name);
	
	List<CityEntity> findByCountry(String country);
	
	boolean existsByName(String name);
}

