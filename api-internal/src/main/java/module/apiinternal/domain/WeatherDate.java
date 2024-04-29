package module.apiinternal.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "WEATHER_DATE")
@SequenceGenerator(name = "SEQ_WEATHER_DATE", sequenceName = "SEQ_WEATHER_DATE_ID", allocationSize = 1)
public class WeatherDate {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WEATHER_DATE")
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
