package com.weather.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
