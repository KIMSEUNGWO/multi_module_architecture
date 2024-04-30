package module.apiexternal.dto;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ResponseJsonItem {

    private String fcstDate;
    private String fcstTime;
    private String category;
    private String fcstValue;

}
