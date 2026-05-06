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

import com.weather.demo.mapper.CityMapper;
import com.weather.demo.model.CityDto;
import com.weather.demo.model.CityEntity;
import com.weather.demo.repository.CityRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("CityService Unit Tests")
class CityServiceTest {

	@Mock
	private CityRepository cityRepository;

	@Mock
	private CityMapper cityMapper;

	@InjectMocks
	private CityService cityService;

	private CityEntity cityEntity;
	private CityDto cityDto;

	@BeforeEach
	void setUp() {
		cityEntity = new CityEntity();
		cityEntity.setId(1L);
		cityEntity.setName("Bucharest");
		cityEntity.setCountry("Romania");
		cityEntity.setLatitude(44.4268);
		cityEntity.setLongitude(26.1025);
		cityEntity.setCreatedAt(Instant.now());

		cityDto = new CityDto();
		cityDto.setId(1L);
		cityDto.setName("Bucharest");
		cityDto.setCountry("Romania");
		cityDto.setLatitude(44.4268);
		cityDto.setLongitude(26.1025);
		cityDto.setCreatedAt(Instant.now());
	}

	@Nested
	@DisplayName("Get Methods")
	class GetMethods {

		@Test
		@DisplayName("Should get all cities successfully")
		void testGetAll() {
			// Given
			List<CityEntity> entities = List.of(cityEntity);
			when(cityRepository.findAll()).thenReturn(entities);
			when(cityMapper.toDto(cityEntity)).thenReturn(cityDto);

			// When
			List<CityDto> result = cityService.getAll();

			// Then
			assertThat(result).hasSize(1);
			assertThat(result.get(0)).isEqualTo(cityDto);
			verify(cityRepository, times(1)).findAll();
		}

		@Test
		@DisplayName("Should get city by ID successfully")
		void testGetById() {
			// Given
			when(cityRepository.findById(1L)).thenReturn(Optional.of(cityEntity));
			when(cityMapper.toDto(cityEntity)).thenReturn(cityDto);

			// When
			Optional<CityDto> result = cityService.getById(1L);

			// Then
			assertThat(result).isPresent();
			assertThat(result.get()).isEqualTo(cityDto);
			verify(cityRepository, times(1)).findById(1L);
		}

		@Test
		@DisplayName("Should get city by name successfully")
		void testGetByName() {
			// Given
			when(cityRepository.findByName("Bucharest")).thenReturn(Optional.of(cityEntity));
			when(cityMapper.toDto(cityEntity)).thenReturn(cityDto);

			// When
			Optional<CityDto> result = cityService.getByName("Bucharest");

			// Then
			assertThat(result).isPresent();
			assertThat(result.get().getName()).isEqualTo("Bucharest");
			verify(cityRepository, times(1)).findByName("Bucharest");
		}

		@Test
		@DisplayName("Should get cities by country successfully")
		void testGetByCountry() {
			// Given
			List<CityEntity> entities = List.of(cityEntity);
			when(cityRepository.findByCountry("Romania")).thenReturn(entities);
			when(cityMapper.toDto(cityEntity)).thenReturn(cityDto);

			// When
			List<CityDto> result = cityService.getByCountry("Romania");

			// Then
			assertThat(result).hasSize(1);
			assertThat(result.get(0).getCountry()).isEqualTo("Romania");
			verify(cityRepository, times(1)).findByCountry("Romania");
		}

		@Test
		@DisplayName("Should get paginated cities successfully")
		void testGetAllPaginated() {
			// Given
			Pageable pageable = PageRequest.of(0, 10);
			Page<CityEntity> page = new PageImpl<>(List.of(cityEntity), pageable, 1);
			when(cityRepository.findAll(pageable)).thenReturn(page);
			when(cityMapper.toDto(cityEntity)).thenReturn(cityDto);

			// When
			Page<CityDto> result = cityService.getAllPaginated(pageable);

			// Then
			assertThat(result).hasSize(1);
			assertThat(result.getTotalElements()).isEqualTo(1);
			verify(cityRepository, times(1)).findAll(pageable);
		}
	}

	@Nested
	@DisplayName("Create Method")
	class CreateMethod {

		@Test
		@DisplayName("Should create city successfully")
		void testCreate() {
			// Given
			CityDto inputDto = new CityDto();
			inputDto.setName("Cluj-Napoca");
			inputDto.setCountry("Romania");
			inputDto.setLatitude(46.7712);
			inputDto.setLongitude(23.6236);

			when(cityRepository.existsByName("Cluj-Napoca")).thenReturn(false);
			when(cityMapper.toEntity(inputDto)).thenReturn(cityEntity);
			when(cityRepository.save(cityEntity)).thenReturn(cityEntity);
			when(cityMapper.toDto(cityEntity)).thenReturn(cityDto);

			// When
			CityDto result = cityService.create(inputDto);

			// Then
			assertThat(result).isNotNull();
			assertThat(result.getId()).isEqualTo(1L);
			verify(cityRepository, times(1)).existsByName("Cluj-Napoca");
			verify(cityRepository, times(1)).save(any(CityEntity.class));
		}

		@Test
		@DisplayName("Should throw exception when city already exists")
		void testCreateDuplicateName() {
			// Given
			CityDto inputDto = new CityDto();
			inputDto.setName("Bucharest");
			
			when(cityRepository.existsByName("Bucharest")).thenReturn(true);

			// When & Then
			assertThatThrownBy(() -> cityService.create(inputDto))
				.isInstanceOf(ResponseStatusException.class);
			verify(cityRepository, times(1)).existsByName("Bucharest");
			verify(cityRepository, never()).save(any());
		}
	}

	@Nested
	@DisplayName("Update Method")
	class UpdateMethod {

		@Test
		@DisplayName("Should update city successfully")
		void testUpdate() {
			// Given
			CityDto updateDto = new CityDto();
			updateDto.setName("Bucharest Updated");
			updateDto.setCountry("Romania");

			CityEntity updatedEntity = new CityEntity();
			updatedEntity.setId(1L);
			updatedEntity.setName("Bucharest Updated");
			updatedEntity.setCountry("Romania");
			updatedEntity.setCreatedAt(cityEntity.getCreatedAt());

			when(cityRepository.findById(1L)).thenReturn(Optional.of(cityEntity));
			when(cityMapper.toEntity(updateDto)).thenReturn(updatedEntity);
			when(cityRepository.save(updatedEntity)).thenReturn(updatedEntity);
			when(cityMapper.toDto(updatedEntity)).thenReturn(new CityDto(1L, "Bucharest Updated", "Romania", 44.4268, 26.1025, Instant.now()));

			// When
			CityDto result = cityService.update(1L, updateDto);

			// Then
			assertThat(result).isNotNull();
			assertThat(result.getId()).isEqualTo(1L);
			verify(cityRepository, times(1)).findById(1L);
			verify(cityRepository, times(1)).save(any(CityEntity.class));
		}

		@Test
		@DisplayName("Should throw exception when city not found on update")
		void testUpdateNotFound() {
			// Given
			CityDto updateDto = new CityDto();
			when(cityRepository.findById(999L)).thenReturn(Optional.empty());

			// When & Then
			assertThatThrownBy(() -> cityService.update(999L, updateDto))
				.isInstanceOf(ResponseStatusException.class);
			verify(cityRepository, times(1)).findById(999L);
			verify(cityRepository, never()).save(any());
		}
	}

	@Nested
	@DisplayName("Delete Method")
	class DeleteMethod {

		@Test
		@DisplayName("Should delete city successfully")
		void testDelete() {
			// Given
			when(cityRepository.existsById(1L)).thenReturn(true);

			// When
			cityService.delete(1L);

			// Then
			verify(cityRepository, times(1)).existsById(1L);
			verify(cityRepository, times(1)).deleteById(1L);
		}

		@Test
		@DisplayName("Should throw exception when city not found on delete")
		void testDeleteNotFound() {
			// Given
			when(cityRepository.existsById(999L)).thenReturn(false);

			// When & Then
			assertThatThrownBy(() -> cityService.delete(999L))
				.isInstanceOf(ResponseStatusException.class);
			verify(cityRepository, times(1)).existsById(999L);
			verify(cityRepository, never()).deleteById(any());
		}
	}
}

