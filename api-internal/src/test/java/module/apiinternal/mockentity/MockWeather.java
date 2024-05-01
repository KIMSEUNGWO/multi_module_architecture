package module.apiinternal.mockentity;

import jakarta.persistence.EntityManager;
import module.apicommon.enums.Category;
import module.apiinternal.domain.Weather;
import module.apiinternal.domain.WeatherDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MockWeather {

    @Autowired
    EntityManager em;

    public MockWeatherBuilder createWeathers() {
        List<Weather> weathers = new ArrayList<>();
        Category[] categories = Category.values();
        for (Category category : categories) {
            Weather build = Weather.builder()
                .category(category)
                .dataValue("0")
                .build();
            weathers.add(build);
        }
        return new MockWeatherBuilder(weathers);
    }


    public class MockWeatherBuilder {

        private final List<Weather> weathers;

        public MockWeatherBuilder(List<Weather> weathers) {
            this.weathers = weathers;
        }

        public MockWeatherBuilder addCategory(Category category, String dataValue) {
            weathers.add(Weather.builder().category(category).dataValue(dataValue).build());
            return this;
        }

        public MockWeatherBuilder setWeatherDate(WeatherDate weatherDate) {
            weathers.iterator().forEachRemaining(x -> x.setWeatherDate(weatherDate));
            return this;
        }

        public List<Weather> build() {
            for (Weather weather : weathers) {
                em.persist(weather);
            }
            em.flush();
            em.clear();
            return weathers;
        }
    }
}
