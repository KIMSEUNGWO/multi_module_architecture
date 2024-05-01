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

    @GetMapping("/" + GET_URI)
    public ResponseEntity<ResponseMessage> findWeatherData(RequestDto requestData) throws NotFoundDataException {

        List<ResultData> resultData = internalService.findAllByDateTime(requestData.getDate());

        return ResponseEntity.ok(new ResponseData<>(OK, SUCCESS_SEARCH, resultData));
    }


    @PostMapping("/" + POST_URI)
    public ResponseEntity<ResponseMessage> externalApiData(@ModelAttribute(REQUEST_DATA_NAME) RequestDto requestData) throws DataException, NotFoundDataException, URISyntaxException {

        List<ResponseJsonItem> jsonDataList = externalService.getJsonData(requestData.getDate());

        DataBinder binder = DataFormatter.dataBinding(jsonDataList);

        internalService.saveData(binder);

        return ResponseEntity.ok(new ResponseMessage(OK, SUCCESS_SAVE_DATA));
    }

}
