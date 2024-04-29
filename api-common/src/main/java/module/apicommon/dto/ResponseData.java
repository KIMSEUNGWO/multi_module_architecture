package module.apicommon.dto;

import lombok.Getter;

@Getter
public class ResponseData<T> extends ResponseMessage {

    private final T data;

    public ResponseData(ResponseMessage message, T data) {
        super(message);
        this.data = data;
    }
    public ResponseData(String result, String message, T data) {
        super(result, message);
        this.data = data;
    }
}
