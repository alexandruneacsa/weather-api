package com.weather.demo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import com.weather.demo.mapper.WeatherMapper;
import com.weather.demo.model.WeatherDto;
import com.weather.demo.model.WeatherEntity;
import com.weather.demo.model.WeatherStatisticsDto;
import com.weather.demo.repository.WeatherRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("WeatherService Unit Tests")
class WeatherServiceTest {

	@Mock
	private WeatherRepository weatherRepository;

	@Mock
	private WeatherMapper weatherMapper;

	@InjectMocks
	private WeatherService weatherService;

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
	@DisplayName("Get Methods")
	class GetMethods {

		@Test
		@DisplayName("Should get all weather records successfully")
		void testGetAll() {
			// Given
			List<WeatherEntity> entities = List.of(weatherEntity);
			when(weatherRepository.findAll()).thenReturn(entities);
			when(weatherMapper.toDto(weatherEntity)).thenReturn(weatherDto);

			// When
			List<WeatherDto> result = weatherService.getAll();

			// Then
			assertThat(result).hasSize(1);
			assertThat(result.get(0)).isEqualTo(weatherDto);
			verify(weatherRepository, times(1)).findAll();
			verify(weatherMapper, times(1)).toDto(weatherEntity);
		}

		@Test
		@DisplayName("Should return empty list when no records exist")
		void testGetAllEmpty() {
			// Given
			when(weatherRepository.findAll()).thenReturn(List.of());

			// When
			List<WeatherDto> result = weatherService.getAll();

			// Then
			assertThat(result).isEmpty();
			verify(weatherRepository, times(1)).findAll();
		}

		@Test
		@DisplayName("Should get weather record by ID successfully")
		void testGetById() {
			// Given
			when(weatherRepository.findById(1L)).thenReturn(Optional.of(weatherEntity));
			when(weatherMapper.toDto(weatherEntity)).thenReturn(weatherDto);

			// When
			Optional<WeatherDto> result = weatherService.getById(1L);

			// Then
			assertThat(result).isPresent();
			assertThat(result).contains(weatherDto);
			verify(weatherRepository, times(1)).findById(1L);
		}

		@Test
		@DisplayName("Should return empty Optional when record not found")
		void testGetByIdNotFound() {
			// Given
			when(weatherRepository.findById(999L)).thenReturn(Optional.empty());

			// When
			Optional<WeatherDto> result = weatherService.getById(999L);

			// Then
			assertThat(result).isEmpty();
			verify(weatherRepository, times(1)).findById(999L);
		}

		@Test
		@DisplayName("Should get paginated weather records successfully")
		void testGetAllPaginated() {
			// Given
			Pageable pageable = PageRequest.of(0, 10);
			Page<WeatherEntity> page = new PageImpl<>(List.of(weatherEntity), pageable, 1);
			when(weatherRepository.findAll(pageable)).thenReturn(page);
			when(weatherMapper.toDto(weatherEntity)).thenReturn(weatherDto);

			// When
			Page<WeatherDto> result = weatherService.getAllPaginated(pageable);

			// Then
			assertThat(result).hasSize(1);
			assertThat(result.getTotalElements()).isEqualTo(1);
			assertThat(result.getNumber()).isZero();
			verify(weatherRepository, times(1)).findAll(pageable);
		}

		@Test
		@DisplayName("Should search weather records by city successfully")
		void testSearchByCity() {
			// Given
			List<WeatherEntity> entities = List.of(weatherEntity);
			when(weatherRepository.findByCity("Bucharest")).thenReturn(entities);
			when(weatherMapper.toDto(weatherEntity)).thenReturn(weatherDto);

			// When
			List<WeatherDto> result = weatherService.searchByCity("Bucharest");

			// Then
			assertThat(result).hasSize(1);
			assertThat(result.get(0).getCity()).isEqualTo("Bucharest");
			verify(weatherRepository, times(1)).findByCity("Bucharest");
		}

		@Test
		@DisplayName("Should return empty list when no city matches")
		void testSearchByCityNotFound() {
			// Given
			when(weatherRepository.findByCity("NonExistent")).thenReturn(List.of());

			// When
			List<WeatherDto> result = weatherService.searchByCity("NonExistent");

			// Then
			assertThat(result).isEmpty();
			verify(weatherRepository, times(1)).findByCity("NonExistent");
		}

		@Test
		@DisplayName("Should get weather by temperature range successfully")
		void testGetByTemperatureRange() {
			// Given
			List<WeatherEntity> entities = List.of(weatherEntity);
			when(weatherRepository.findByTemperatureBetween(20.0, 30.0)).thenReturn(entities);
			when(weatherMapper.toDto(weatherEntity)).thenReturn(weatherDto);

			// When
			List<WeatherDto> result = weatherService.getByTemperatureRange(20.0, 30.0);

			// Then
			assertThat(result).hasSize(1);
			verify(weatherRepository, times(1)).findByTemperatureBetween(20.0, 30.0);
		}

		@Test
		@DisplayName("Should get weather statistics by city successfully")
		void testGetStatistics() {
			// Given
			WeatherStatisticsDto stats = new WeatherStatisticsDto("Bucharest", 25.5, 20.0, 30.0, 5L);
			when(weatherRepository.getStatisticsByCity("Bucharest")).thenReturn(Optional.of(stats));

			// When
			WeatherStatisticsDto result = weatherService.getStatistics("Bucharest");

			// Then
			assertThat(result.getCity()).isEqualTo("Bucharest");
			assertThat(result.getAverageTemperature()).isEqualTo(25.5);
			assertThat(result.getRecordCount()).isEqualTo(5L);
			verify(weatherRepository, times(1)).getStatisticsByCity("Bucharest");
		}

		@Test
		@DisplayName("Should throw ResponseStatusException when statistics not found")
		void testGetStatisticsNotFound() {
			// Given
			when(weatherRepository.getStatisticsByCity("NonExistent")).thenReturn(Optional.empty());

			// When & Then
			assertThatThrownBy(() -> weatherService.getStatistics("NonExistent"))
				.isInstanceOf(ResponseStatusException.class);
			verify(weatherRepository, times(1)).getStatisticsByCity("NonExistent");
		}
	}

	@Nested
	@DisplayName("Create Method")
	class CreateMethod {

		@Test
		@DisplayName("Should create weather record successfully")
		void testCreate() {
			// Given
			WeatherDto inputDto = new WeatherDto();
			inputDto.setCity("Bucharest");
			inputDto.setTemperature(25.5);
			inputDto.setPressure(1013);
			inputDto.setHumidity(65);

			when(weatherMapper.toEntity(inputDto)).thenReturn(weatherEntity);
			when(weatherRepository.save(weatherEntity)).thenReturn(weatherEntity);
			when(weatherMapper.toDto(weatherEntity)).thenReturn(weatherDto);

			// When
			WeatherDto result = weatherService.create(inputDto);

			// Then
			assertThat(result).isNotNull();
			assertThat(result.getId()).isEqualTo(1L);
			assertThat(result.getCity()).isEqualTo("Bucharest");
			verify(weatherRepository, times(1)).save(any(WeatherEntity.class));
		}

		@Test
		@DisplayName("Should set ID to null before creating")
		void testCreateSetsIdToNull() {
			// Given
			WeatherDto inputDto = new WeatherDto();
			inputDto.setId(999L); // Should be ignored
			inputDto.setCity("Bucharest");

			when(weatherMapper.toEntity(inputDto)).thenReturn(weatherEntity);
			when(weatherRepository.save(weatherEntity)).thenReturn(weatherEntity);
			when(weatherMapper.toDto(weatherEntity)).thenReturn(weatherDto);

			// When
			WeatherDto result = weatherService.create(inputDto);

			// Then
			assertThat(result.getId()).isEqualTo(1L);
			verify(weatherRepository, times(1)).save(any(WeatherEntity.class));
		}
	}

	@Nested
	@DisplayName("Update Method")
	class UpdateMethod {

		@Test
		@DisplayName("Should update weather record successfully")
		void testUpdate() {
			// Given
			WeatherDto updateDto = new WeatherDto();
			updateDto.setCity("Bucharest");
			updateDto.setTemperature(26.5);

			WeatherEntity existingEntity = new WeatherEntity();
			existingEntity.setId(1L);
			existingEntity.setCity("Bucharest");
			existingEntity.setTemperature(25.5);

			WeatherEntity updatedEntity = new WeatherEntity();
			updatedEntity.setId(1L);
			updatedEntity.setCity("Bucharest");
			updatedEntity.setTemperature(26.5);

			when(weatherRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
			when(weatherMapper.toEntity(updateDto)).thenReturn(updatedEntity);
			when(weatherRepository.save(updatedEntity)).thenReturn(updatedEntity);
			when(weatherMapper.toDto(updatedEntity)).thenReturn(new WeatherDto(1L, "Bucharest", 26.5, 1013, 65, Instant.now()));

			// When
			WeatherDto result = weatherService.update(1L, updateDto);

			// Then
			assertThat(result).isNotNull();
			assertThat(result.getId()).isEqualTo(1L);
			verify(weatherRepository, times(1)).findById(1L);
			verify(weatherRepository, times(1)).save(any(WeatherEntity.class));
		}

		@Test
		@DisplayName("Should throw ResponseStatusException when record not found on update")
		void testUpdateNotFound() {
			// Given
			WeatherDto updateDto = new WeatherDto();
			when(weatherRepository.findById(999L)).thenReturn(Optional.empty());

			// When & Then
			assertThatThrownBy(() -> weatherService.update(999L, updateDto))
				.isInstanceOf(ResponseStatusException.class);
			verify(weatherRepository, times(1)).findById(999L);
			verify(weatherRepository, never()).save(any());
		}
	}

	@Nested
	@DisplayName("Delete Method")
	class DeleteMethod {

		@Test
		@DisplayName("Should delete weather record successfully")
		void testDelete() {
			// Given
			when(weatherRepository.existsById(1L)).thenReturn(true);

			// When
			weatherService.delete(1L);

			// Then
			verify(weatherRepository, times(1)).existsById(1L);
			verify(weatherRepository, times(1)).deleteById(1L);
		}

		@Test
		@DisplayName("Should throw ResponseStatusException when record not found on delete")
		void testDeleteNotFound() {
			// Given
			when(weatherRepository.existsById(999L)).thenReturn(false);

			// When & Then
			assertThatThrownBy(() -> weatherService.delete(999L))
				.isInstanceOf(ResponseStatusException.class);
			verify(weatherRepository, times(1)).existsById(999L);
			verify(weatherRepository, never()).deleteById(any());
		}
	}
}

