package com.weather.demo.controller;

import java.util.List;
import java.util.Optional;

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

import com.weather.demo.model.CityDto;
import com.weather.demo.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {
	
	private final CityService cityService;
	
	@Operation(summary = "Get all cities.")
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<CityDto> list() {
		return cityService.getAll();
	}
	
	@Operation(summary = "Get city by ID.")
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Optional<CityDto> getById(@PathVariable Long id) {
		return cityService.getById(id);
	}
	
	@Operation(summary = "Get city by name.")
	@GetMapping("/search/name")
	@ResponseStatus(HttpStatus.OK)
	public Optional<CityDto> getByName(@RequestParam String name) {
		return cityService.getByName(name);
	}
	
	@Operation(summary = "Get cities by country.")
	@GetMapping("/search/country")
	@ResponseStatus(HttpStatus.OK)
	public List<CityDto> getByCountry(@RequestParam String country) {
		return cityService.getByCountry(country);
	}
	
	@Operation(summary = "Get all cities with pagination.")
	@GetMapping("/paginated")
	@ResponseStatus(HttpStatus.OK)
	public Page<CityDto> getAllPaginated(Pageable pageable) {
		return cityService.getAllPaginated(pageable);
	}
	
	@Operation(summary = "Create a new city.")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CityDto create(@RequestBody CityDto dto) {
		return cityService.create(dto);
	}
	
	@Operation(summary = "Update an existing city.")
	@PutMapping("/{id}")
	public CityDto update(@PathVariable Long id, @RequestBody CityDto dto) {
		return cityService.update(id, dto);
	}
	
	@Operation(summary = "Delete a city by ID.")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		cityService.delete(id);
	}
}

