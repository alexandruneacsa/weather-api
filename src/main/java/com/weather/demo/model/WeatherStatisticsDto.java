package com.weather.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherStatisticsDto {
	
	private String city;
	private Double averageTemperature;
	private Double minTemperature;
	private Double maxTemperature;
	private Long recordCount;
}

