package module.apiexternal.dto;

import module.apiexternal.json.JsonItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface JsonMapper {

    JsonMapper INSTANCE = Mappers.getMapper(JsonMapper.class);

    List<ResponseJsonItem> toResponseJsonItem(List<JsonItem> itemList);
}
