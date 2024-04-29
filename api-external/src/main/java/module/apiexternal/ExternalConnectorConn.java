package module.apiexternal;

import module.apiexternal.component.CustomTemplate;
import module.apiexternal.json.JsonData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.net.URISyntaxException;

@Repository
@RequiredArgsConstructor
public class ExternalConnectorConn implements ExternalConnector {

    private final String URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
    private final String SERVICE_KEY = "pmfTJaX3B9gDDbzwfn50OCLnYh821OEjP%2BilHLYlj8aJTAtSquEAc%2FWwHmxiNo3TzBeWuODnY4cu2vUOOQxxMw%3D%3D";

    private final CustomTemplate customTemplate;
    @Override
    public JsonData getJsonData(String date) throws URISyntaxException {
        return customTemplate.get(new URI(getURL(date)), JsonData.class);
    }

    private String getURL(String date) {
        StringBuilder sb = new StringBuilder(URL);  // URL
        sb.append("?serviceKey=" + SERVICE_KEY);    // Service Key
        sb.append("&pageNo=1");                     // 페이지번호
        sb.append("&numOfRows=12");                 // 한 페이지 결과 수 1시간 마다 12개의 정보가 들어가있음
        sb.append("&dataType=JSON");                // 요청자료형식(XML/JSON) Default: XML
        sb.append("&base_date=").append(date);      // ‘21년 6월 28일발표
        sb.append("&base_time=0500");               // 05시 발표
        sb.append("&nx=55");                        // 예보지점의 X 좌표값
        sb.append("&ny=127");                       // 예보지점의 Y 좌표값
        return sb.toString();
    }
}