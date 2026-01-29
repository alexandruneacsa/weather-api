package com.weather.demo.mapper;

import com.weather.demo.config.GlobalMapperConfig;
import com.weather.demo.model.WeatherDto;
import com.weather.demo.model.WeatherEntity;
import com.weather.demo.model.WeatherEntity;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class)
public interface WeatherMapper {

    WeatherDto toDto(WeatherEntity weatherEntity);
    WeatherEntity toEntity(WeatherDto dto);
}
