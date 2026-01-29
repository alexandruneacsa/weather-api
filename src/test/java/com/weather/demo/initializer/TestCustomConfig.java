package com.weather.demo.initializer;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@TestConfiguration
public class TestCustomConfig {
	
	@Bean
	public MockMvc mockMvcBuilderCustomizer(WebApplicationContext webApplicationContext) {
		
		return MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}		
}
