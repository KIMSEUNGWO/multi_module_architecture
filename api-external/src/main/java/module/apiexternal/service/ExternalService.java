package module.apiexternal.service;

import module.apicommon.enums.ErrorCode;
import module.apicommon.exceptions.DataException;
import module.apicommon.exceptions.NotFoundDataException;
import module.apiexternal.dto.JsonMapper;
import module.apiexternal.repository.ExternalConnector;
import module.apiexternal.dto.ResponseJsonItem;
import module.apiexternal.json.JsonData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExternalService {

    private final ExternalConnector externalConnector;

    public List<ResponseJsonItem> getJsonData(String requestData) throws DataException, NotFoundDataException, URISyntaxException {
        JsonData body = externalConnector.getJsonData(requestData, JsonData.class);

        jsonDataValid(body);

        return JsonMapper.INSTANCE.toResponseJsonItem(body.getItemList());
    }

    private void jsonDataValid(JsonData body) throws DataException, NotFoundDataException {
        if (body == null) throw new DataException(ErrorCode.C04);
        ErrorCode code = body.getCode();
        if (code == ErrorCode.C00) return;
        if (code == ErrorCode.C03) throw new NotFoundDataException(); // 데이터가 없음
        throw new DataException(code); // 정상이 아니면 예외발생
    }
}
