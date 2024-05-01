package module.apiinternal.mockentity;

import jakarta.persistence.EntityManager;
import module.apiinternal.domain.WeatherDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class MockWeatherDate {

    @Autowired
    EntityManager em;


    public MockWeatherDateBuilder createWeatherDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate parse = LocalDate.parse(date, formatter);
        LocalDateTime dateTime = LocalDateTime.of(parse, LocalTime.MIN);
        WeatherDate weatherDate = new WeatherDate(dateTime);
        em.persist(weatherDate);
        return new MockWeatherDateBuilder(weatherDate);
    }

    public class MockWeatherDateBuilder {

        private final WeatherDate weatherDate;

        public MockWeatherDateBuilder(WeatherDate weatherDate) {
            this.weatherDate = weatherDate;
        }

        public WeatherDate build() {
            em.flush();
            em.clear();
            return weatherDate;
        }


    }
}
