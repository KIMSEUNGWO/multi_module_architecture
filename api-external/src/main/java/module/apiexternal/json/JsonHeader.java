package module.apiexternal.json;

import module.apicommon.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class JsonHeader {

    private String resultCode;
    private String resultMsg;

    public ErrorCode getCode() {
        return ErrorCode.getCode(resultCode);
    }
}
