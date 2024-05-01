package module.apiexternal.service;

import module.apicommon.enums.ErrorCode;
import module.apicommon.exceptions.DataException;
import module.apicommon.exceptions.NotFoundDataException;
import module.apiexternal.ApiExternalTestApplication;
import module.apiexternal.dto.ResponseJsonItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest(classes = ApiExternalTestApplication.class)
@TestPropertySource("classpath:application-external.properties")
public class ExternalServiceAPITest {


    @Autowired
    ExternalService externalService;


    @Test
    @DisplayName("정상조회")
    void getJsonDataApiTest() throws DataException, NotFoundDataException, URISyntaxException {
        /*
           3일전까지 데이터를 API 조회가 가능하지만,
           시간에 따라 당일을 포함 해 2~3일전의 데이터가 존재하지 않을 가능성이 다수 존재하기 때문에
           1일 전만 테스트
        */
        // given
        String currentDate = getCurrentDate(1);

        // when
        List<ResponseJsonItem> jsonData = externalService.getJsonData(currentDate);

        // then
        assertThat(jsonData).isNotNull();
    }

    @Test
    @DisplayName("데이터가 존재하지 않으면 NotFoundDataException 예외가 발생한다.")
    void getJsonDataApiTest2() {
        // given
        String currentDate = getCurrentDate(-1); // 다음날

        // then
        assertThatThrownBy(() -> externalService.getJsonData(currentDate))
                .isInstanceOf(NotFoundDataException.class);
    }

    @Test
    @DisplayName("3일 이내가 아니라면 DataException 예외가 발생한다.")
    void getJsonDataApiTest3() {
        // given
        String currentDate = getCurrentDate(4); // 4일 전

        // then
        assertThatThrownBy(() -> externalService.getJsonData(currentDate))
                .isInstanceOf(DataException.class)
                .hasFieldOrPropertyWithValue("code", ErrorCode.C10);
    }


    private String getCurrentDate(int minusDay) {
        LocalDate now = LocalDate.now().minusDays(minusDay);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return now.format(formatter);
    }

}