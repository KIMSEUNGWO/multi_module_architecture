package module.apiexternal;

import module.apicommon.dto.ResponseData;
import module.apicommon.dto.ResponseMessage;
import module.apicommon.exceptions.DataException;
import module.apicommon.exceptions.NotFoundDataException;
import module.apiexternal.dto.DataFormatter;
import module.apiexternal.dto.ResponseJsonItem;
import module.apiexternal.service.ExternalService;
import module.apiinternal.dto.DataBinder;
import module.apiinternal.service.InternalService;
import module.apiexternal.dto.RequestDto;
import module.apiinternal.dto.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

import static module.apicommon.dto.APIConst.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(PROJECT_NAME + "/" + VERSION)
public class Controller {

    private final ExternalService externalService;
    private final InternalService internalService;
    /**
     * 데이터(date = 20241231) 를 요청받으면 해당하는 날짜의 날씨예보 데이터를 반환
     * @param requestData : RequestParam 으로 데이터를 전송받음.
     * @return 요청받은 날짜에 데이터가 존재하면 예보데이터와 함께 HttpStatus 202 반환
     * @throws NotFoundDataException : 데이터가 존재하지 않으면 HttpStatus 204 반환
     */
    @GetMapping("/" + GET_URI)
    public ResponseEntity<ResponseMessage> findWeatherData(RequestDto requestData) throws NotFoundDataException {

        List<ResultData> resultData = internalService.findData(requestData.getDate());

        return ResponseEntity.ok(new ResponseData<>(OK, SUCCESS_SEARCH, resultData));
    }

    /**
     * 데이터(date = 20241231) 를 요청받으면 공공데이터포탈 단기예보 API에서 해당 날짜의 데이터를 DB에 저장한다.
     * @param requestData MessageBody 에 담기는 요청데이터
     * @return 외부 API에서 조회한 데이터가 DB에 저장되면 성공메세지와 HttpStatus 202 반환
     * @throws DataException 단기예보 API 자체가 존재하지 않는 경우(null) ErrorCode.C04와 HttpStatus 400 반환
     * @throws NotFoundDataException 데이터(date = 20241231)로 단기예보 API 에서 조회한 결과가 존재하지않으면 HttpStatus 204 반환
     * @throws URISyntaxException 단기예보 API 요청주소가 올바르지 않으면 발생하는 내부예외
     */
    @PostMapping("/" + POST_URI)
    public ResponseEntity<ResponseMessage> externalApiData(@ModelAttribute(REQUEST_DATA_NAME) RequestDto requestData) throws DataException, NotFoundDataException, URISyntaxException {

        List<ResponseJsonItem> jsonDataList = externalService.getJsonData(requestData.getDate());

        DataBinder binder = DataFormatter.dataBinding(jsonDataList);

        internalService.saveData(binder);

        return ResponseEntity.ok(new ResponseMessage(OK, SUCCESS_SAVE_DATA));
    }

}
