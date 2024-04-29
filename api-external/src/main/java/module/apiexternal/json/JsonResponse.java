package module.apiexternal.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import module.apicommon.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonResponse {

    private JsonHeader header;
    private JsonBody body;


    public List<JsonItem> getItemList() {
        return body.getItemList();
    }

    public ErrorCode getCode() {
        return header.getCode();
    }
}
