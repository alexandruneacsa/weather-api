package com.weather.demo.service;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.weather.demo.model.WeatherStatisticsDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.weather.demo.mapper.WeatherMapper;
import com.weather.demo.model.WeatherDto;
import com.weather.demo.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {
	
	private final WeatherRepository repository;
	private final WeatherMapper     weatherMapper;
	
	public List<WeatherDto> getAll() {
		
		return repository.findAll()
			.stream()
			.map(weatherMapper::toDto)
			.collect(Collectors.toList());
	}
	
	public Optional<WeatherDto> getById(Long id) {
		
		return repository.findById(id)
			.map(weatherMapper::toDto);
	}
	
	public WeatherDto create(WeatherDto dto) {
		
		dto.setId(null);
		var entity = weatherMapper.toEntity(dto);
		var saved = repository.save(entity);
		
		return weatherMapper.toDto(saved);
	}
	
	public WeatherDto update(Long id, WeatherDto dto) {
		
		var existing = repository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Weather record not found: " + id));
		dto.setId(id);
		var toSave = weatherMapper.toEntity(dto);
		toSave.setId(existing.getId());
		var saved = repository.save(toSave);
		
		return weatherMapper.toDto(saved);
	}
	
	public void delete(Long id) {
		
		if(!repository.existsById(id)) {
			throw new ResponseStatusException(NOT_FOUND, "Weather record not found: " + id);
		}
		
		repository.deleteById(id);
	}
	
	public Page<WeatherDto> getAllPaginated(Pageable pageable) {
		log.info("Fetching paginated weather records: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
		return repository.findAll(pageable)
			.map(weatherMapper::toDto);
	}
	
	public List<WeatherDto> searchByCity(String city) {
		log.info("Searching weather records for city: {}", city);
		return repository.findByCity(city)
			.stream()
			.map(weatherMapper::toDto)
			.collect(Collectors.toList());
	}
	
	public List<WeatherDto> getByTemperatureRange(Double minTemp, Double maxTemp) {
		log.info("Fetching weather records with temperature between {} and {}", minTemp, maxTemp);
		return repository.findByTemperatureBetween(minTemp, maxTemp)
			.stream()
			.map(weatherMapper::toDto)
			.collect(Collectors.toList());
	}
	
	public WeatherStatisticsDto getStatistics(String city) {
		log.info("Fetching statistics for city: {}", city);
		return repository.getStatisticsByCity(city)
			.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "No data found for city: " + city));
	}
}
