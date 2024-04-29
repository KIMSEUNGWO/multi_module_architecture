package module.apicommon.dto;

import module.apicommon.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseMessage {

    private final String result;
    private final String message;

    public ResponseMessage(ResponseMessage message) {
        this.result = message.result;
        this.message = message.message;
    }

    public ResponseMessage(ErrorCode code) {
        this.result = code.getErrorMsg();
        this.message = code.getMessage();
    }
}

