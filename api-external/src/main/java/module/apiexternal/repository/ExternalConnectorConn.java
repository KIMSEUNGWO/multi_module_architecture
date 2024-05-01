package module.apiexternal.repository;

import module.apiexternal.component.CustomTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.net.URISyntaxException;

@Repository
@RequiredArgsConstructor
public class ExternalConnectorConn implements ExternalConnector {

    private final String URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";

    @Value("${SERVICE_KEY}")
    private String SERVICE_KEY;

    private final CustomTemplate customTemplate;
    @Override
    public <T> T getJsonData(String date, Class<T> clazz) throws URISyntaxException {
        URI uri = new URI(getURL(date));
        return customTemplate.get(uri, clazz);
    }

    private String getURL(String date) {
        StringBuilder sb = new StringBuilder(URL);  // URL
        sb.append("?serviceKey=" + SERVICE_KEY);    // Service Key
        sb.append("&pageNo=1");                     // 페이지번호
        sb.append("&numOfRows=").append(12 * 24);   // 한 페이지 결과 수 / 1시간 마다 12개의 정보가 들어가있음
        sb.append("&dataType=JSON");                // 요청자료형식(XML/JSON) Default: XML
        sb.append("&base_date=").append(date);      // ‘21년 6월 28일발표
        sb.append("&base_time=0500");               // 05시 발표
        sb.append("&nx=55");                        // 예보지점의 X 좌표값
        sb.append("&ny=127");                       // 예보지점의 Y 좌표값
        return sb.toString();
    }
}