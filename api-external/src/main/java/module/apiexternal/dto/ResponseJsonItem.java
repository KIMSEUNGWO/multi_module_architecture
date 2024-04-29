package module.apiexternal.dto;

import module.apiexternal.json.JsonItem;
import lombok.Getter;

@Getter
public class ResponseJsonItem {

    private final String fcstDate;
    private final String fcstTime;
    private final String category;
    private final String fcstValue;

    public ResponseJsonItem(JsonItem item) {
        this.fcstDate = item.getFcstDate();
        this.fcstTime = item.getFcstTime();
        this.category = item.getCategory();
        this.fcstValue = item.getFcstValue();
    }
}
