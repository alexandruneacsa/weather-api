package com.weather.demo.mapper;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.weather.demo.model.WeatherDto;
import com.weather.demo.model.WeatherEntity;

@SpringBootTest
@DisplayName("WeatherMapper Tests")
class WeatherMapperTest {

	@Autowired
	private WeatherMapper weatherMapper;

	private WeatherEntity weatherEntity;
	private WeatherDto weatherDto;

	@BeforeEach
	void setUp() {
		weatherEntity = new WeatherEntity();
		weatherEntity.setId(1L);
		weatherEntity.setCity("Bucharest");
		weatherEntity.setTemperature(25.5);
		weatherEntity.setPressure(1013);
		weatherEntity.setHumidity(65);
		weatherEntity.setObservedAt(Instant.now());

		weatherDto = new WeatherDto();
		weatherDto.setId(1L);
		weatherDto.setCity("Bucharest");
		weatherDto.setTemperature(25.5);
		weatherDto.setPressure(1013);
		weatherDto.setHumidity(65);
		weatherDto.setObservedAt(Instant.now());
	}

	@Nested
	@DisplayName("Entity to DTO Mapping")
	class EntityToDtoMapping {

		@Test
		@DisplayName("Should map WeatherEntity to WeatherDto successfully")
		void testToDtoMapping() {
			// When
			WeatherDto result = weatherMapper.toDto(weatherEntity);

			// Then
			assertThat(result).isNotNull();
			assertThat(result.getId()).isEqualTo(weatherEntity.getId());
			assertThat(result.getCity()).isEqualTo(weatherEntity.getCity());
			assertThat(result.getTemperature()).isEqualTo(weatherEntity.getTemperature());
			assertThat(result.getPressure()).isEqualTo(weatherEntity.getPressure());
			assertThat(result.getHumidity()).isEqualTo(weatherEntity.getHumidity());
			assertThat(result.getObservedAt()).isEqualTo(weatherEntity.getObservedAt());
		}

		@Test
		@DisplayName("Should map all fields correctly")
		void testToDtoMappingAllFields() {
			// When
			WeatherDto result = weatherMapper.toDto(weatherEntity);

			// Then
			assertThat(result)
				.extracting("id", "city", "temperature", "pressure", "humidity")
				.containsExactly(1L, "Bucharest", 25.5, 1013, 65);
		}

		@Test
		@DisplayName("Should handle null entity gracefully")
		void testToDtoWithNullEntity() {
			// When & Then
			assertThatNoException().isThrownBy(() -> weatherMapper.toDto(null));
		}

		@Test
		@DisplayName("Should preserve null values in mapping")
		void testToDtoPreserveNullValues() {
			// Given
			WeatherEntity entityWithNulls = new WeatherEntity();
			entityWithNulls.setId(2L);
			entityWithNulls.setCity("TestCity");

			// When
			WeatherDto result = weatherMapper.toDto(entityWithNulls);

			// Then
			assertThat(result.getId()).isEqualTo(2L);
			assertThat(result.getCity()).isEqualTo("TestCity");
			assertThat(result.getTemperature()).isNull();
			assertThat(result.getPressure()).isNull();
			assertThat(result.getHumidity()).isNull();
		}
	}

	@Nested
	@DisplayName("DTO to Entity Mapping")
	class DtoToEntityMapping {

		@Test
		@DisplayName("Should map WeatherDto to WeatherEntity successfully")
		void testToEntityMapping() {
			// When
			WeatherEntity result = weatherMapper.toEntity(weatherDto);

			// Then
			assertThat(result).isNotNull();
			assertThat(result.getId()).isEqualTo(weatherDto.getId());
			assertThat(result.getCity()).isEqualTo(weatherDto.getCity());
			assertThat(result.getTemperature()).isEqualTo(weatherDto.getTemperature());
			assertThat(result.getPressure()).isEqualTo(weatherDto.getPressure());
			assertThat(result.getHumidity()).isEqualTo(weatherDto.getHumidity());
			assertThat(result.getObservedAt()).isEqualTo(weatherDto.getObservedAt());
		}

		@Test
		@DisplayName("Should map all DTO fields to entity fields correctly")
		void testToEntityMappingAllFields() {
			// When
			WeatherEntity result = weatherMapper.toEntity(weatherDto);

			// Then
			assertThat(result)
				.extracting("id", "city", "temperature", "pressure", "humidity")
				.containsExactly(1L, "Bucharest", 25.5, 1013, 65);
		}

		@Test
		@DisplayName("Should handle null DTO gracefully")
		void testToEntityWithNullDto() {
			// When & Then
			assertThatNoException().isThrownBy(() -> weatherMapper.toEntity(null));
		}

		@Test
		@DisplayName("Should preserve null values when mapping DTO to entity")
		void testToEntityPreserveNullValues() {
			// Given
			WeatherDto dtoWithNulls = new WeatherDto();
			dtoWithNulls.setId(3L);
			dtoWithNulls.setCity("AnotherCity");

			// When
			WeatherEntity result = weatherMapper.toEntity(dtoWithNulls);

			// Then
			assertThat(result.getId()).isEqualTo(3L);
			assertThat(result.getCity()).isEqualTo("AnotherCity");
			assertThat(result.getTemperature()).isNull();
			assertThat(result.getPressure()).isNull();
			assertThat(result.getHumidity()).isNull();
		}
	}

	@Nested
	@DisplayName("Bidirectional Mapping")
	class BidirectionalMapping {

		@Test
		@DisplayName("Should preserve data when mapping entity -> dto -> entity")
		void testBidirectionalEntityMapping() {
			// When
			WeatherDto dto = weatherMapper.toDto(weatherEntity);
			WeatherEntity resultEntity = weatherMapper.toEntity(dto);

			// Then
			assertThat(resultEntity.getCity()).isEqualTo(weatherEntity.getCity());
			assertThat(resultEntity.getTemperature()).isEqualTo(weatherEntity.getTemperature());
			assertThat(resultEntity.getPressure()).isEqualTo(weatherEntity.getPressure());
			assertThat(resultEntity.getHumidity()).isEqualTo(weatherEntity.getHumidity());
		}

		@Test
		@DisplayName("Should preserve data when mapping dto -> entity -> dto")
		void testBidirectionalDtoMapping() {
			// When
			WeatherEntity entity = weatherMapper.toEntity(weatherDto);
			WeatherDto resultDto = weatherMapper.toDto(entity);

			// Then
			assertThat(resultDto.getCity()).isEqualTo(weatherDto.getCity());
			assertThat(resultDto.getTemperature()).isEqualTo(weatherDto.getTemperature());
			assertThat(resultDto.getPressure()).isEqualTo(weatherDto.getPressure());
			assertThat(resultDto.getHumidity()).isEqualTo(weatherDto.getHumidity());
		}
	}

	@Nested
	@DisplayName("Data Type Handling")
	class DataTypeHandling {

		@Test
		@DisplayName("Should handle different temperature values correctly")
		void testTemperatureMapping() {
			// Given
			weatherEntity.setTemperature(-5.3);

			// When
			WeatherDto result = weatherMapper.toDto(weatherEntity);

			// Then
			assertThat(result.getTemperature()).isEqualTo(-5.3);
		}

		@Test
		@DisplayName("Should handle pressure values correctly")
		void testPressureMapping() {
			// Given
			weatherEntity.setPressure(1050);

			// When
			WeatherDto result = weatherMapper.toDto(weatherEntity);

			// Then
			assertThat(result.getPressure()).isEqualTo(1050);
		}

		@Test
		@DisplayName("Should handle humidity values correctly")
		void testHumidityMapping() {
			// Given
			weatherEntity.setHumidity(100);

			// When
			WeatherDto result = weatherMapper.toDto(weatherEntity);

			// Then
			assertThat(result.getHumidity()).isEqualTo(100);
		}

		@Test
		@DisplayName("Should handle Instant values correctly")
		void testInstantMapping() {
			// Given
			Instant now = Instant.now();
			weatherEntity.setObservedAt(now);

			// When
			WeatherDto result = weatherMapper.toDto(weatherEntity);

			// Then
			assertThat(result.getObservedAt()).isEqualTo(now);
		}

		@Test
		@DisplayName("Should handle city names with special characters")
		void testCityNameWithSpecialCharacters() {
			// Given
			weatherEntity.setCity("Bra??ov");

			// When
			WeatherDto result = weatherMapper.toDto(weatherEntity);

			// Then
			assertThat(result.getCity()).isEqualTo("Bra??ov");
		}
	}

	@Nested
	@DisplayName("Edge Cases")
	class EdgeCases {

		@Test
		@DisplayName("Should map entity with zero values")
		void testMappingZeroValues() {
			// Given
			weatherEntity.setTemperature(0.0);
			weatherEntity.setPressure(0);
			weatherEntity.setHumidity(0);

			// When
			WeatherDto result = weatherMapper.toDto(weatherEntity);

			// Then
			assertThat(result.getTemperature()).isEqualTo(0.0);
			assertThat(result.getPressure()).isEqualTo(0);
			assertThat(result.getHumidity()).isEqualTo(0);
		}

		@Test
		@DisplayName("Should map entity with maximum double values")
		void testMappingMaxDoubleValues() {
			// Given
			weatherEntity.setTemperature(Double.MAX_VALUE);

			// When
			WeatherDto result = weatherMapper.toDto(weatherEntity);

			// Then
			assertThat(result.getTemperature()).isEqualTo(Double.MAX_VALUE);
		}

		@Test
		@DisplayName("Should map entity with empty string city")
		void testMappingEmptyStringCity() {
			// Given
			weatherEntity.setCity("");

			// When
			WeatherDto result = weatherMapper.toDto(weatherEntity);

			// Then
			assertThat(result.getCity()).isEmpty();
		}
	}
}

