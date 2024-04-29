package module.apiexternal.json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class JsonItem {

    private String baseDate;
    private String baseTime;
    private String fcstDate; // 예보일자
    private String fcstTime; // 예보시각
    private String category; // 자료구분문자
    private String fcstValue; // 예보 값

    private int nx;
    private int ny;
}
