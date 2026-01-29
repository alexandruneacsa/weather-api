package com.weather.demo.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDto {
	
	private Long    id;
	private String  city;
	private Double  temperature;
	private Integer pressure;
	private Integer humidity;
	private Instant observedAt;
}
