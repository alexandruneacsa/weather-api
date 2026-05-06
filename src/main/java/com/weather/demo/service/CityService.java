package com.weather.demo.service;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.weather.demo.mapper.CityMapper;
import com.weather.demo.model.CityDto;
import com.weather.demo.model.CityEntity;
import com.weather.demo.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityService {
	
	private final CityRepository repository;
	private final CityMapper mapper;
	
	public List<CityDto> getAll() {
		log.info("Fetching all cities");
		return repository.findAll()
			.stream()
			.map(mapper::toDto)
			.collect(Collectors.toList());
	}
	
	public Optional<CityDto> getById(Long id) {
		log.info("Fetching city by id: {}", id);
		return repository.findById(id)
			.map(mapper::toDto);
	}
	
	public Optional<CityDto> getByName(String name) {
		log.info("Fetching city by name: {}", name);
		return repository.findByName(name)
			.map(mapper::toDto);
	}
	
	public List<CityDto> getByCountry(String country) {
		log.info("Fetching cities by country: {}", country);
		return repository.findByCountry(country)
			.stream()
			.map(mapper::toDto)
			.collect(Collectors.toList());
	}
	
	public Page<CityDto> getAllPaginated(Pageable pageable) {
		log.info("Fetching paginated cities: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
		return repository.findAll(pageable)
			.map(mapper::toDto);
	}
	
	public CityDto create(CityDto dto) {
		log.info("Creating city: {}", dto.getName());
		
		if (repository.existsByName(dto.getName())) {
			throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "City already exists: " + dto.getName());
		}
		
		dto.setId(null);
		var entity = mapper.toEntity(dto);
		var saved = repository.save(entity);
		
		log.info("City created with id: {}", saved.getId());
		return mapper.toDto(saved);
	}
	
	public CityDto update(Long id, CityDto dto) {
		log.info("Updating city with id: {}", id);
		
		var existing = repository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "City not found: " + id));
		
		dto.setId(id);
		var toSave = mapper.toEntity(dto);
		toSave.setCreatedAt(existing.getCreatedAt());
		var saved = repository.save(toSave);
		
		log.info("City updated with id: {}", saved.getId());
		return mapper.toDto(saved);
	}
	
	public void delete(Long id) {
		log.info("Deleting city with id: {}", id);
		
		if (!repository.existsById(id)) {
			throw new ResponseStatusException(NOT_FOUND, "City not found: " + id);
		}
		
		repository.deleteById(id);
	}
}

