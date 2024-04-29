package module.apiexternal.json;

import module.apicommon.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class JsonData {

    private JsonResponse response;

    public ErrorCode getCode() {
        return response.getCode();
    }

    public List<JsonItem> getItemList() {
        return response.getItemList();
    }
}
