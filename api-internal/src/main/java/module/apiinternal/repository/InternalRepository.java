package module.apiinternal.repository;

import module.apiinternal.domain.Weather;
import module.apiinternal.domain.WeatherDate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface InternalRepository {

    void saveWeatherDate(WeatherDate saveWeatherDate);
    void saveAllWeather(Collection<Weather> weathers);

    List<WeatherDate> findAll(LocalDateTime dateTime);

    boolean existsByDateTime(LocalDateTime dateTime);
}
