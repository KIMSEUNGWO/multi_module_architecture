package module.apiexternal.service;

import module.apicommon.enums.Category;
import module.apicommon.enums.ErrorCode;
import module.apicommon.exceptions.DataException;
import module.apicommon.exceptions.NotFoundDataException;
import module.apiexternal.ApiExternalTestApplication;
import module.apiexternal.dto.ResponseJsonItem;
import module.apiexternal.json.*;
import module.apiexternal.mockentity.MockJsonData;
import module.apiexternal.repository.ExternalConnectorConn;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ApiExternalTestApplication.class)
public class ExternalServiceMockTest {

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
        JsonData mockJsonData = MockJsonData.createJsonData()
                .setErrorCode("03")
                .setBody()
                .setJsonItem()
                .build();

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
        JsonData mockJsonData = MockJsonData.createJsonData()
                .setErrorCode("200")
                .build();

        // when
        Mockito.when(externalConnector.getJsonData(date, JsonData.class)).thenReturn(mockJsonData);

        // then
        Assertions.assertThatThrownBy(() -> externalService.getJsonData(date))
            .isInstanceOf(DataException.class)
            .hasFieldOrPropertyWithValue("code", ErrorCode.C99);
    }

    @Test
    @DisplayName("errorCode 가 00이 아니라면 DataException 예외가 발생해야한다.")
    void getJsonDataTest4() throws URISyntaxException {
        // given
        String date = "20240429";
        JsonData mockJsonData = MockJsonData.createJsonData()
                .setErrorCode("01")
                .build();

        // when
        Mockito.when(externalConnector.getJsonData(date, JsonData.class)).thenReturn(mockJsonData);

        // then
        Assertions.assertThatThrownBy(() -> externalService.getJsonData(date))
                .isInstanceOf(DataException.class)
                .hasFieldOrPropertyWithValue("code", ErrorCode.C01);
    }

    @Test
    @DisplayName("errorCode 가 00이면 정상흐름이 되어야 한다.")
    void getJsonDataTest5() throws URISyntaxException, DataException, NotFoundDataException {
        // given
        String date = "20240429";
        JsonData mockJsonData = MockJsonData.createJsonData()
                .setErrorCode("00")
                .setBody()
                .setJsonItem()
                .build();

        // when
        Mockito.when(externalConnector.getJsonData(date, JsonData.class)).thenReturn(mockJsonData);

        // then
        List<ResponseJsonItem> jsonData = externalService.getJsonData(date);
        assertThat(jsonData).isNotNull();
    }

    @Test
    @DisplayName("정상흐름일때 JsonItem -> ResponseJsonItem으로 Convert 가 되어야 한다.")
    void getJsonDataTest6() throws URISyntaxException, DataException, NotFoundDataException {
        // given
        String date = "20240429";
        Category category = Category.PTY;

        JsonData mockJsonData = MockJsonData.createJsonData()
                .setErrorCode("00")
                .setBody()
                .setJsonItem(date, "0600", category, "4") // 강수형태 4 (소나기)
                .build();

        // when
        Mockito.when(externalConnector.getJsonData(date, JsonData.class)).thenReturn(mockJsonData);
        List<ResponseJsonItem> jsonData = externalService.getJsonData(date);

        // then
        assertThat(jsonData.size()).isEqualTo(1);
        assertThat(jsonData.get(0))
                .hasFieldOrPropertyWithValue("fcstDate", "20240429")
                .hasFieldOrPropertyWithValue("fcstTime", "0600")
                .hasFieldOrPropertyWithValue("category", category.name())
                .hasFieldOrPropertyWithValue("fcstValue", "4");

    }

}