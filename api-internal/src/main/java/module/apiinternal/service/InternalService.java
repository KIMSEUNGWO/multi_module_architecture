package module.apiinternal.service;

import module.apicommon.exceptions.NotFoundDataException;
import module.apiinternal.domain.WeatherDate;
import module.apiinternal.dto.DataBinder;
import module.apiinternal.dto.ResultData;
import module.apiinternal.repository.InternalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static module.apicommon.dto.DateConverter.convertDateTime;

@Service
@RequiredArgsConstructor
public class InternalService {

    private final InternalRepository internalRepository;

    public List<ResultData> findData(String date) throws NotFoundDataException {
        List<WeatherDate> findWeatherDate = internalRepository.findAll(convertDateTime(date));
        if (findWeatherDate.isEmpty()) throw new NotFoundDataException();

        return convertResultData(findWeatherDate);
    }

    private List<ResultData> convertResultData(List<WeatherDate> findWeatherDate) {
        List<ResultData> responseList = new ArrayList<>(findWeatherDate.size());
        for (WeatherDate weatherDate : findWeatherDate) {
            responseList.add(new ResultData(weatherDate));
        }
        return responseList;
    }

    public void saveData(DataBinder dataBinder) {
        for (String key : dataBinder) {
            LocalDateTime dateTime = convertDateTime(key);
            boolean exists = internalRepository.existsByDateTime(dateTime);
            if (exists) continue;

            WeatherDate saveWeatherDate = new WeatherDate(dateTime);
            internalRepository.saveWeatherDate(saveWeatherDate);

            dataBinder.setWeatherDate(key, saveWeatherDate);
            internalRepository.saveAllWeather(dataBinder.get(key));

        }
    }
}
