package module.apiinternal.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "WEATHER_DATE")
public class WeatherDate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weatherDateId;

    @Getter
    @Column(nullable = false)
    private LocalDateTime dateTime;

    @OneToMany(mappedBy = "weatherDate", fetch = FetchType.LAZY)
    private List<Weather> weathers;

    public WeatherDate(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<String> getWeathersToString() {
        List<String> temp = new ArrayList<>(weathers.size());
        for (Weather weather : weathers) {
            temp.add(weather.getInfo());
        }
        return temp;
    }
}
