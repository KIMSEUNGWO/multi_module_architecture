package module.apicommon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    C00("NORMAL_SERVICE", "정상"),
    C01("APPLICATION_ERROR", "어플리케이션에 문제가 발생했습니다."),
    C02("DB_ERROR", "공공데이터포탈 DB에서 문제가 발생했습니다."),
    C03("NODATA_ERROR", "데이터가 없습니다."),
    C04("HTTP_ERROR", "HTTP 연결에 문제가 생겼습니다."),
    C05("SERVICETIME_OUT", "예보조회서비스 연결에 실패했습니다."),
    C10("INVALID_REQUEST_PARAMETER_ERROR", "최근 3일 간의 자료만 제공합니다."),
    C11("NO_MANATORY_REQUEST_PARAMETERS_ERROR", "필수요청 파라미터가 존재하지 않습니다."),
    C12("NO_OPENAPI_SERVICE_ERROR", "해당 API 서비스가 폐기되었습니다."),
    C20("SERVICE_ACCESS_DENIED_ERROR", "예보조회서비스 접근이 거부되었습니다."),
    C21("TEMPORARILY_DISABLE_THE_SERVICEKEY_ERROR", "서비스키의 접근이 일시적으로 제한되었습니다."),
    C22("LIMITED_NUMBER_OF_SERVICE_REQUESTS_EXCEEDS_ERROR", ("서비스 요청제한횟수가 초과되었습니다.")),
    C30("SERVICE_KEY_IS_NOT_REGISTERED_ERROR", "등록되지 않은 서비스키입니다."),
    C31("DEADLINE_HAS_EXPIRED_ERROR", "기한이 만료된 서비스키입니다."),
    C32("UNREGISTERED_IP_ERROR", "등록되지 않은 IP입니다."),
    C33("UNSIGNED_CALL_ERROR", "서명되지 않은 호출입니다."),
    C99("UNKNOWN_ERROR", "기타 에러가 발생했습니다."),

    C100("UNKNOWN_EXCEPTION_ERROR", "에러코드를 확인할 수 없습니다."),
    C101("INVALID_REQUEST_PARAMETER_ERROR", "요청 파라미터가 잘못되었습니다."),
    C102("INVALID_URI", "요청 주소가 올바르지 않습니다.");


    private final String errorMsg;
    private final String message;


    public static ErrorCode getCode(String resultCode) {
        String findName = String.format("C%s", resultCode);

        ErrorCode[] values = values();
        for (ErrorCode value : values) {
            if (value.name().equals(findName)) return value;
        }
        return C99;
    }
}
