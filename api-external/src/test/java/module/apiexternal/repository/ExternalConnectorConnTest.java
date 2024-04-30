package module.apiexternal.repository;

import module.apiexternal.ApiExternalTestApplication;
import module.apiexternal.component.CustomTemplate;
import module.apiexternal.json.JsonData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.net.URI;
import java.net.URISyntaxException;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = ApiExternalTestApplication.class)
@TestPropertySource("classpath:application-external.properties")
class ExternalConnectorConnTest {


    @Mock
    CustomTemplate customTemplate;
    @InjectMocks
    ExternalConnectorConn externalConnector;

    @Test
    void getJsonData() throws URISyntaxException {
        // given
        String date = "";
        JsonData mockedResponse = new JsonData();

        // when
        when(customTemplate.get(any(URI.class), eq(JsonData.class))).thenReturn(mockedResponse);
        JsonData result = externalConnector.getJsonData(date, JsonData.class);

        // then
        Assertions.assertThat(result).isNotNull();

    }

}