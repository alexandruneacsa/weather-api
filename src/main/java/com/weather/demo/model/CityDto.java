package com.weather.demo.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDto {
	
	private Long id;
	private String name;
	private String country;
	private Double latitude;
	private Double longitude;
	private Instant createdAt;
}

