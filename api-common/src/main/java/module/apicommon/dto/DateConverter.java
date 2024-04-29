package module.apicommon.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateConverter {

    /**
     * 예보일(fcstDate)과 예보시간(fcstTime) 을 합치는 함수
     * @param fcstDate : 연월일(yyyyMMdd) ex) 20241231
     * @param fcstTime : 시분(HHmm) ex) 2359
     * @return : 연월일시분(yyyyMMddHHmm) ex) 202412312359
     */
    public static String format(String fcstDate, String fcstTime) {
        return String.format("%s%s", fcstDate, fcstTime);
    }

    public static String format(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }

    /**
     * format 된 연월일시분 문자열을 LocalDateTime 으로 변경하는 함수
     * @param dateStr : yyyyMMddHHmm or yyyyMMdd
     * @return : LocalDateTime
     */
    public static LocalDateTime convertDateTime(String dateStr) {
        if (dateStr.length() == 8) {
            return LocalDateTime.of(convertDate(dateStr), LocalTime.MIN);
        }
        return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
    }

    /**
     * format 된 연월일 문자열을 LocalDate 으로 변경하는 함수
     * @param dateStr : 연월일(yyyyMMdd) ex) 20001231
     * @return : LocalDate
     */
    private static LocalDate convertDate(String dateStr) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    /**
     * 단기예보 데이터는 최근 3일간의 데이터만 보관하기때문에 사용되는 요청날짜문자열 검증 함수
     * @param date : 연월일(yyyyMMdd) ex) 20001231
     * @return 요청날짜가 현재기준으로 3일 이내이면 true, 아니면 false
     */
    public static boolean isValidFormatDate(String date) {
        LocalDate convertDate = convertDate(date);
        LocalDate now = LocalDate.now();
        return !(convertDate.isAfter(now) || convertDate.isBefore(now.minusDays(3)));
    }
}
