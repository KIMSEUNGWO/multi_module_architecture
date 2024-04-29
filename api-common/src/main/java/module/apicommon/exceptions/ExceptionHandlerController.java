package module.apicommon.exceptions;

import module.apicommon.dto.ResponseMessage;
import module.apicommon.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URISyntaxException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(DataException.class)
    public ResponseEntity<ResponseMessage> dataExceptionHandler(DataException e) {
        log.error("DataException 발생! 에러코드 : {}", e.getCode());
        e.printStackTrace();
        return ResponseEntity.badRequest().body(new ResponseMessage(e.getCode()));
    }

    @ExceptionHandler(NotFoundDataException.class)
    public ResponseEntity<ResponseMessage> notFoundDataExceptionHandler() {
        log.error("NotFoundDataException 발생!");
        return ResponseEntity.noContent().build();
    }

    /**
     * RequestDto date 파라미터가 존재하지 않는경우 에러코드 C11을 사용한다.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseMessage> missingServletRequestParameterException() {
        log.error("MissingServletRequestParameterException 발생! Request 데이터가 존재하지 않습니다.");
        return ResponseEntity.badRequest().body(new ResponseMessage(ErrorCode.C11));
    }

    /**
     * 올바르지 않은 주소를 URI 객체로 변경한 경우
     */
    @ExceptionHandler(URISyntaxException.class)
    public ResponseEntity<ResponseMessage> uriSyntaxException(URISyntaxException e) {
        log.error("URISyntaxException 발생! URI로 변환하는 과정에서 예외발생");
        e.printStackTrace();
        return ResponseEntity.badRequest().body(new ResponseMessage(ErrorCode.C102));
    }
}
