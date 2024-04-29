package module.apiinternal.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    @Getter
    @OneToMany(mappedBy = "weatherDate", fetch = FetchType.LAZY)
    private List<Weather> weathers;

    public WeatherDate(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
