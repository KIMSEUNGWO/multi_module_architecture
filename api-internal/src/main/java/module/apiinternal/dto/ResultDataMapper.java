package module.apiinternal.dto;

import module.apicommon.dto.DateConverter;
import module.apiinternal.domain.WeatherDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ResultDataMapper {

    ResultDataMapper INSTANCE = Mappers.getMapper(ResultDataMapper.class);

    @Mapping(target = "date", source = "dateTime", qualifiedByName = "convertDate")
    @Mapping(target = "detailData", source = "weathersToString")
    ResultData toResultData(WeatherDate weatherDate);

    @Named("convertDate")
    static String localDateTimeToString(LocalDateTime dateTime) {
        return DateConverter.format(dateTime);
    }

    default List<ResultData> toResultDataList(List<WeatherDate> weatherDateList) {
        return weatherDateList.stream()
            .map(this::toResultData)
            .toList();
    }



}
