package module.apiexternal.dto;

import module.apicommon.dto.DateConverter;
import module.apicommon.enums.Category;
import module.apiinternal.domain.Weather;
import module.apiinternal.dto.DataBinder;

import java.util.List;

public class DataFormatter {

    public static DataBinder dataBinding(List<ResponseJsonItem> jsonDataList) {
        DataBinder dataBinder = new DataBinder();

        for (ResponseJsonItem item : jsonDataList) {
            String date = DateConverter.format(item.getFcstDate(), item.getFcstTime());
            Weather build = Weather.builder()
                .category(Category.valueOf(item.getCategory()))
                .dataValue(item.getFcstValue())
                .build();
            dataBinder.put(date, build);
        }
        return dataBinder;
    }

}
