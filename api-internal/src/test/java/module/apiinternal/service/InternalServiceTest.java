package module.apiinternal.service;

import module.apicommon.dto.DateConverter;
import module.apicommon.enums.Category;
import module.apicommon.exceptions.NotFoundDataException;
import module.apiinternal.config.InternalTestConfig;
import module.apiinternal.domain.Weather;
import module.apiinternal.domain.WeatherDate;
import module.apiinternal.dto.DataBinder;
import module.apiinternal.dto.ResultData;
import module.apiinternal.mockentity.MockWeather;
import module.apiinternal.mockentity.MockWeatherDate;
import module.apiinternal.repository.InternalRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-internal-test.properties")
@Import(InternalTestConfig.class)
public class InternalServiceTest {

    @Autowired
    InternalService internalService;
    @Autowired
    InternalRepository internalRepository;

    @Autowired
    MockWeather mockWeather;
    @Autowired
    MockWeatherDate mockWeatherDate;

    @Test
    @DisplayName("DB에 해당 데이터가 존재하지 않으면 NotFoundDataException이 발생해야 한다.")
    void findAllByDateTimeTest1() {
        // given
        String date = "20240101";

        // then
        Assertions.assertThatThrownBy(() -> internalService.findAllByDateTime(date))
            .isInstanceOf(NotFoundDataException.class);
    }
    @Test
    @DisplayName("findAllByDateTime 정상흐름")
    void findAllByDateTimeTest2() throws NotFoundDataException {
        // given
        String date = "20240101";
        WeatherDate weatherDate = mockWeatherDate.createWeatherDate(date).build();
        List<Weather> weathers = mockWeather.createWeathers().setWeatherDate(weatherDate).build();

        // when
        List<ResultData> allByDateTime = internalService.findAllByDateTime(date);

        // then
        assertThat(allByDateTime.size()).isEqualTo(1);
        String data = allByDateTime.get(0).getDate();
        assertThat(data).isEqualTo("2024-01-01 00:00");
    }
    @Test
    @DisplayName("findAllByDateTime dateTime between 경계값 확인")
    void findAllByDateTimeTest3() throws NotFoundDataException {
        // given
        String date = "20240101";
        WeatherDate weatherDate = mockWeatherDate.createWeatherDate(date).build();
        List<Weather> weathers = mockWeather.createWeathers().setWeatherDate(weatherDate).build();
        String date2 = "20240102";
        WeatherDate weatherDate2 = mockWeatherDate.createWeatherDate(date2).build();
        List<Weather> weathers2 = mockWeather.createWeathers().setWeatherDate(weatherDate2).build();

        // when
        List<ResultData> allByDateTime = internalService.findAllByDateTime(date);

        // then
        assertThat(allByDateTime.size()).isEqualTo(1);
        String data = allByDateTime.get(0).getDate();
        assertThat(data).isEqualTo("2024-01-01 00:00");
    }

    @Test
    @DisplayName("saveData 정상흐름")
    void saveDataTest1() {
        // given
        String date = "20240101";
        String time = "0100";
        String dateFormat = DateConverter.format(date, time);
        Weather weather = Weather.builder().category(Category.PCP).dataValue("1").build();

        DataBinder dataBinder = new DataBinder();
        dataBinder.put(dateFormat, weather);

        // when
        internalService.saveData(dataBinder);

        // then
        LocalDateTime dateTime = DateConverter.convertDateTime(date);
        List<WeatherDate> allByDateTime = internalRepository.findAllByDateTime(dateTime);
        assertThat(allByDateTime.size()).isEqualTo(1);
    }
    @Test
    @DisplayName("saveData 정상흐름2")
    void saveDataTest2() {
        // given
        String date1 = "20240101";
        String date2 = "20240102";
        String time = "0000";

        DataBinder dataBinder = new DataBinder();
        dataBinder.put(DateConverter.format(date1, time), Weather.builder().category(Category.PCP).dataValue("1").build());
        dataBinder.put(DateConverter.format(date2, time), Weather.builder().category(Category.PCP).dataValue("1").build());

        // when
        internalService.saveData(dataBinder);

        // then
        LocalDateTime dateTime1 = DateConverter.convertDateTime(date1);
        LocalDateTime dateTime2 = DateConverter.convertDateTime(date2);
        List<WeatherDate> allByDateTime1 = internalRepository.findAllByDateTime(dateTime1);
        List<WeatherDate> allByDateTime2 = internalRepository.findAllByDateTime(dateTime2);
        assertThat(allByDateTime1.size()).isEqualTo(1);
        assertThat(allByDateTime2.size()).isEqualTo(1);
    }

}