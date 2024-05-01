package module.apiinternal.config;

import module.apiinternal.jparepository.JpaWeatherDateRepository;
import module.apiinternal.jparepository.JpaWeatherRepository;
import module.apiinternal.mockentity.MockWeather;
import module.apiinternal.mockentity.MockWeatherDate;
import module.apiinternal.repository.InternalRepository;
import module.apiinternal.repository.InternalRepositoryDB;
import module.apiinternal.service.InternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class InternalTestConfig {

    @Autowired
    JpaWeatherDateRepository jpaWeatherDateRepository;
    @Autowired
    JpaWeatherRepository jpaWeatherRepository;

    @Bean
    InternalService internalService() {
        return new InternalService(internalRepository());
    }

    @Bean
    InternalRepository internalRepository() {
        return new InternalRepositoryDB(jpaWeatherRepository, jpaWeatherDateRepository);
    }

    @Bean
    MockWeather mockWeather() {
        return new MockWeather();
    }

    @Bean
    MockWeatherDate weatherDate() {
        return new MockWeatherDate();
    }




}
