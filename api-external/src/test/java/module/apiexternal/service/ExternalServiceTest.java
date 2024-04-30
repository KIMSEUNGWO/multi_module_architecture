package module.apiexternal.service;

import module.apicommon.enums.ErrorCode;
import module.apicommon.exceptions.DataException;
import module.apicommon.exceptions.NotFoundDataException;
import module.apiexternal.config.ApiExternalTestConfiguration;
import module.apiexternal.dto.ResponseJsonItem;
import module.apiexternal.json.*;
import module.apiexternal.repository.ExternalConnectorConn;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = ApiExternalTestConfiguration.class)
public class ExternalServiceTest {

    @Mock
    ExternalConnectorConn externalConnector;

    @InjectMocks
    ExternalService externalService;


    @Test
    @DisplayName("JsonData가 null이면 DataException(ErrorCode.C04) 예외가 발생되어야 한다.")
    void getJsonDataTest() throws URISyntaxException {
        // given
        String date = "20240429";
        JsonData mockedResponse = null;

        // when
        Mockito.when(externalConnector.getJsonData(date, JsonData.class)).thenReturn(mockedResponse);

        // then
        Assertions.assertThatThrownBy(() -> externalService.getJsonData(date))
            .isInstanceOf(DataException.class)
            .hasFieldOrPropertyWithValue("code", ErrorCode.C04);
    }


    @Test
    @DisplayName("errorCode 가 03이면 NotFountException이 발생되어야 한다.")
    void getJsonDataTest2() throws URISyntaxException {
        // given
        String date = "20240429";
        JsonData mockJsonData = new JsonData();
        JsonResponse mockJsonResponse = new JsonResponse();
        JsonHeader mockJsonHeader = new JsonHeader();
        mockJsonHeader.setResultCode("03");
        mockJsonHeader.setResultMsg("");
        mockJsonResponse.setHeader(mockJsonHeader);
        mockJsonData.setResponse(mockJsonResponse);

        // when
        Mockito.when(externalConnector.getJsonData(date, JsonData.class)).thenReturn(mockJsonData);

        // then
        Assertions.assertThatThrownBy(() -> externalService.getJsonData(date))
            .isInstanceOf(NotFoundDataException.class);
    }

    @Test
    @DisplayName("errorCode 가 ErrorCode enum에 존재하지 않는다면 DataException(C99) 예외가 발생해야한다.")
    void getJsonDataTest3() throws URISyntaxException {
        // given
        String date = "20240429";
        JsonData mockJsonData = new JsonData();
        JsonResponse mockJsonResponse = new JsonResponse();
        JsonHeader mockJsonHeader = new JsonHeader();
        mockJsonHeader.setResultCode("200");
        mockJsonHeader.setResultMsg("");
        mockJsonResponse.setHeader(mockJsonHeader);
        mockJsonData.setResponse(mockJsonResponse);

        // when
        Mockito.when(externalConnector.getJsonData(date, JsonData.class)).thenReturn(mockJsonData);

        // then
        Assertions.assertThatThrownBy(() -> externalService.getJsonData(date))
            .isInstanceOf(DataException.class)
            .hasFieldOrPropertyWithValue("code", ErrorCode.C99);
    }

    @Test
    @DisplayName("errorCode 가 00이면 정상흐름이 되어야 한다.")
    void getJsonDataTest4() throws URISyntaxException, DataException, NotFoundDataException {
        // given
        String date = "20240429";
        JsonData mockJsonData = new JsonData();
        JsonResponse mockJsonResponse = new JsonResponse();
        JsonHeader mockJsonHeader = new JsonHeader();
        mockJsonHeader.setResultCode("00");
        mockJsonHeader.setResultMsg("");
        mockJsonResponse.setHeader(mockJsonHeader);

        JsonBody mockJsonBody = new JsonBody();
        JsonItems mockJsonItems = new JsonItems();
        List<JsonItem> mockJsonItemList = new ArrayList<>();
        mockJsonItems.setItem(mockJsonItemList);
        mockJsonBody.setItems(mockJsonItems);
        mockJsonResponse.setBody(mockJsonBody);

        mockJsonData.setResponse(mockJsonResponse);

        // when
        Mockito.when(externalConnector.getJsonData(date, JsonData.class)).thenReturn(mockJsonData);

        // then
        List<ResponseJsonItem> jsonData = externalService.getJsonData(date);
        Assertions.assertThat(jsonData).isNotNull();
    }

}