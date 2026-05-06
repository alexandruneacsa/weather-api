package com.weather.demo.mapper;

import org.mapstruct.Mapper;

import com.weather.demo.model.CityDto;
import com.weather.demo.model.CityEntity;

@Mapper(componentModel = "spring")
public interface CityMapper {
	
	CityDto toDto(CityEntity entity);
	
	CityEntity toEntity(CityDto dto);
}

