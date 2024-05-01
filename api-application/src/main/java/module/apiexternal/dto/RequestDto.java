package module.apiexternal.dto;

import module.apicommon.dto.DateConverter;
import module.apicommon.enums.ErrorCode;
import module.apicommon.exceptions.DataException;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@ToString
public class RequestDto {

    private final String date;

    public RequestDto(String date) throws DataException {
        if (date == null) {
            LocalDateTime dateTime = LocalDateTime.now().minusDays(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            date = dateTime.format(formatter);
        }
        validDate(date);
        this.date = date;
    }

    public void validDate(String date) throws DataException {
        // 문자열 검증
        if (date == null || isNaN(date) || date.length() != 8) {
            throw new DataException(ErrorCode.C101);
        }

        // 3일 이내 날짜인지 검증
        if (!DateConverter.isValidFormatDate(date)) {
            throw new DataException(ErrorCode.C10);
        }
    }

    private boolean isNaN(String date) {
        if (date.isBlank()) return true;
        for (int i = 0; i < date.length(); i++) {
            int c = date.charAt(i) - '0';
            if (c < 0 || c > 9) return true;
        }
        return false;
    }
}
