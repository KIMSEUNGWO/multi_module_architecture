package module.apicommon.exceptions;

import module.apicommon.enums.ErrorCode;
import lombok.Getter;

@Getter
public class DataException extends Exception{

    private final ErrorCode code;

    public DataException(ErrorCode code) {
        this.code = code;
    }
}
