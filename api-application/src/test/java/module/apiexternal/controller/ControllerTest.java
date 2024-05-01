package module.apiexternal.controller;

import module.apicommon.exceptions.DataException;
import module.apiexternal.Controller;
import module.apiexternal.dto.RequestDto;
import module.apiexternal.service.ExternalService;
import module.apiinternal.service.InternalService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static module.apicommon.dto.APIConst.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = Controller.class)
class ControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    InternalService internalService;
    @MockBean
    ExternalService externalService;

    @Test
    void findWeatherDataTest() throws Exception {
        String date = "20240501";
        String uri = "/" + PROJECT_NAME + "/" + VERSION + "/" + GET_URI;
        ResultActions resultActions = getResultActionsGET(uri, date);

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("result").value(OK))
            .andExpect(jsonPath("message").value(SUCCESS_SEARCH))
            .andExpect(jsonPath("data").isArray())
            .andDo(print());

    }

    @Test
    void externalApiDataTest() throws Exception {
        String date = "20240501";
        String uri = "/" + PROJECT_NAME + "/" + VERSION + "/" + POST_URI;
        ResultActions resultActions = getResultActionsPOST(uri, date);

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("result").value(OK))
            .andExpect(jsonPath("message").value(SUCCESS_SAVE_DATA))
            .andDo(print());

    }

    @DisplayName("RequestDto Data 예외검증")
    @ParameterizedTest
    @ValueSource(strings = {"!@#12312", "", "12", " 12", "       1", "         ", "        ", " ", "a1d123123", "a1234567"})
    void RequestDtoTest(String date) {
        Assertions.assertThatThrownBy(() -> new RequestDto(date))
            .isInstanceOf(DataException.class);
    }

    @Test
    @DisplayName("RequestDto date 가 null 이면 예외가발생하지 않는다.")
    void RequestDtoTest2() {
        String date = null;
        Assertions.assertThatCode(() -> new RequestDto(date))
            .doesNotThrowAnyException();
    }


    private ResultActions getResultActionsGET(String uri, String date) throws Exception {
        return mvc.perform(get(uri)
            .param("date", date)
            .contentType(MediaType.APPLICATION_JSON));
    }
    private ResultActions getResultActionsPOST(String uri, String date) throws Exception {
        return mvc.perform(post(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .content(date)
        );
    }

}