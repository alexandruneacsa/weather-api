package com.weather.demo;

import org.springframework.boot.SpringApplication;
import org.testcontainers.utility.TestcontainersConfiguration;

public class TestWeatherApplication {

	public static void main(String[] args) {
		SpringApplication.from(WeatherApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
