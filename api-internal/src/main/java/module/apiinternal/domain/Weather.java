package module.apiinternal.domain;

import module.apicommon.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "WEATHER")
@SequenceGenerator(name = "SEQ_WEATHER", sequenceName = "SEQ_WEATHER_ID", allocationSize = 1)
public class Weather {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WEATHER")
    private Long weatherId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DATE_ID")
    @Setter
    private WeatherDate weatherDate;

    @Enumerated(EnumType.STRING)
    private Category category;
    private String dataValue;

    public String getInfo() {
        return category.combineValue(dataValue);
    }

}
