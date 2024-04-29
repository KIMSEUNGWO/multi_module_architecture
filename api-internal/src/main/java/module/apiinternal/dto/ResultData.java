package module.apiinternal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import module.apiinternal.domain.Weather;
import module.apiinternal.domain.WeatherDate;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import module.apicommon.dto.DateConverter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class ResultData {

    private final String date;
    private final List<String> detailData;

    public ResultData(WeatherDate weatherDate) {
        this.date = DateConverter.format(weatherDate.getDateTime());
        this.detailData = convertWeather(weatherDate.getWeathers());
    }

    private List<String> convertWeather(List<Weather> weathers) {
        List<String> temp = new ArrayList<>(weathers.size());

        for (Weather weather : weathers) {
            temp.add(weather.getInfo());
        }

        return temp;
    }
}
