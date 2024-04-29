package module.apiinternal.dto;

import module.apiinternal.domain.Weather;
import module.apiinternal.domain.WeatherDate;

import java.util.*;

public class DataBinder implements Iterable<String> {

    private final Map<String, List<Weather>> data = new HashMap<>();

    public void put(String dateStr, Weather weather) {
        if (!data.containsKey(dateStr)) data.put(dateStr, new ArrayList<>());
        data.get(dateStr).add(weather);
    }

    @Override
    public Iterator<String> iterator() {
        return data.keySet().iterator();
    }

    public List<Weather> get(String dateStr) {
        return data.get(dateStr);
    }

    public void setWeatherDate(String key, WeatherDate saveWeatherDate) {
        data.get(key).iterator().forEachRemaining(weather -> weather.setWeatherDate(saveWeatherDate));
    }
}
