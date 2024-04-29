package module.apiinternal.jparepository;

import module.apiinternal.domain.WeatherDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaWeatherDateRepository extends JpaRepository<WeatherDate, Long> {

    List<WeatherDate> findAllByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    boolean existsByDateTime(LocalDateTime dateTime);
}
