package module.apiinternal.jparepository;

import module.apiinternal.domain.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaWeatherRepository extends JpaRepository<Weather, Long> {
}
