package module.apiinternal.repository;

import module.apiinternal.domain.Weather;
import module.apiinternal.domain.WeatherDate;
import module.apiinternal.jparepository.JpaWeatherDateRepository;
import module.apiinternal.jparepository.JpaWeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class InternalRepositoryDB implements InternalRepository {

    private final JpaWeatherRepository jpaWeatherRepository;
    private final JpaWeatherDateRepository jpaWeatherDateRepository;

    @Override
    @Transactional
    public void saveWeatherDate(WeatherDate saveWeatherDate) {
        jpaWeatherDateRepository.save(saveWeatherDate);
    }

    @Override
    @Transactional
    public void saveAllWeather(Collection<Weather> weathers) {
        jpaWeatherRepository.saveAll(weathers);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WeatherDate> findAllByDateTime(LocalDateTime dateTime) {
        LocalDate localDate = dateTime.toLocalDate();
        LocalTime localTime = LocalTime.of(23, 59, 59);
        LocalDateTime endDate = LocalDateTime.of(localDate, localTime);
        return jpaWeatherDateRepository.findAllByDateTimeBetween(dateTime, endDate);
    }

    @Override
    public boolean existsByDateTime(LocalDateTime dateTime) {
        return jpaWeatherDateRepository.existsByDateTime(dateTime);
    }
}
