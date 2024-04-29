package module.apiinternal.repository;

import module.apiinternal.domain.Weather;
import module.apiinternal.domain.WeatherDate;
import module.apiinternal.jparepository.JpaWeatherDateRepository;
import module.apiinternal.jparepository.JpaWeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public List<WeatherDate> findAll(LocalDateTime dateTime) {
        LocalDateTime endDate = dateTime.plusHours(23);
        return jpaWeatherDateRepository.findAllByDateTimeBetween(dateTime, endDate);
    }

    @Override
    public boolean existsByDateTime(LocalDateTime dateTime) {
        return jpaWeatherDateRepository.existsByDateTime(dateTime);
    }
}
