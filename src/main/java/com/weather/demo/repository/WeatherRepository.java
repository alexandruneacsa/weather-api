package com.weather.demo.repository;

import com.weather.demo.model.WeatherEntity;
import com.weather.demo.model.WeatherStatisticsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity, Long> {
	
	List<WeatherEntity> findByCity(String city);
	
	List<WeatherEntity> findByTemperatureBetween(Double minTemp, Double maxTemp);
	
	@Query("SELECT new com.weather.demo.model.WeatherStatisticsDto(" +
	       "w.city, AVG(w.temperature), MIN(w.temperature), MAX(w.temperature), COUNT(w)) " +
	       "FROM WeatherEntity w WHERE w.city = :city GROUP BY w.city")
	Optional<WeatherStatisticsDto> getStatisticsByCity(@Param("city") String city);
}
