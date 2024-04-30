package module.apiinternal.service;

import module.apicommon.exceptions.NotFoundDataException;
import module.apiinternal.domain.WeatherDate;
import module.apiinternal.dto.DataBinder;
import module.apiinternal.dto.ResultData;
import module.apiinternal.dto.ResultDataMapper;
import module.apiinternal.repository.InternalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static module.apicommon.dto.DateConverter.convertDateTime;

@Service
@RequiredArgsConstructor
public class InternalService {

    private final InternalRepository internalRepository;

    public List<ResultData> findData(String date) throws NotFoundDataException {
        List<WeatherDate> findWeatherDate = internalRepository.findAll(convertDateTime(date));

        if (findWeatherDate.isEmpty()) throw new NotFoundDataException();

        return ResultDataMapper.INSTANCE.toResultDataList(findWeatherDate);
    }

    public void saveData(DataBinder dataBinder) {
        dataBinder.iterator().forEachRemaining(key -> {
            LocalDateTime dateTime = convertDateTime(key);
            boolean exists = internalRepository.existsByDateTime(dateTime);
            if (!exists) {
                WeatherDate saveWeatherDate = new WeatherDate(dateTime);
                internalRepository.saveWeatherDate(saveWeatherDate);

                dataBinder.setWeatherDate(key, saveWeatherDate);
                internalRepository.saveAllWeather(dataBinder.get(key));
            }
        });
    }
}
