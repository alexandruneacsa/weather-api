package com.weather.demo.controller;

import java.util.List;
import java.util.Optional;

import com.weather.demo.model.WeatherStatisticsDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.weather.demo.model.WeatherDto;
import com.weather.demo.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {
	
	private final WeatherService weatherService;
	
	@Operation(summary = "Get all weather records.")
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<WeatherDto> list() {
		
		return weatherService.getAll();
	}
	
	@Operation(summary = "Get weather record by ID.")
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Optional<WeatherDto> getById(@PathVariable Long id) {
		
		return weatherService.getById(id);
	}
	
	@Operation(summary = "Create a new weather record.")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public WeatherDto create(@RequestBody WeatherDto dto) {
		
		return weatherService.create(dto);
	}
	
	@Operation(summary = "Update an existing weather record.")
	@PutMapping("/{id}")
	public WeatherDto update(@PathVariable Long id, @RequestBody WeatherDto dto) {
		
		return weatherService.update(id, dto);
	}
	
	@Operation(summary = "Delete a weather record by ID.")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		
		weatherService.delete(id);
	}
	
	@Operation(summary = "Get all weather records with pagination.")
	@GetMapping("/paginated")
	@ResponseStatus(HttpStatus.OK)
	public Page<WeatherDto> getAllPaginated(Pageable pageable) {
		
		return weatherService.getAllPaginated(pageable);
	}
	
	@Operation(summary = "Search weather records by city.")
	@GetMapping("/search/city")
	@ResponseStatus(HttpStatus.OK)
	public List<WeatherDto> searchByCity(@RequestParam String city) {
		
		return weatherService.searchByCity(city);
	}
	
	@Operation(summary = "Get weather records by temperature range.")
	@GetMapping("/search/temperature")
	@ResponseStatus(HttpStatus.OK)
	public List<WeatherDto> getByTemperatureRange(
		@RequestParam Double minTemp,
		@RequestParam Double maxTemp) {
		
		return weatherService.getByTemperatureRange(minTemp, maxTemp);
	}
	
	@Operation(summary = "Get weather statistics for a city.")
	@GetMapping("/statistics/{city}")
	@ResponseStatus(HttpStatus.OK)
	public WeatherStatisticsDto getStatistics(@PathVariable String city) {
		
		return weatherService.getStatistics(city);
	}
}
