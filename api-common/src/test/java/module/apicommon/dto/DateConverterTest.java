package module.apicommon.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.*;


class DateConverterTest {

    @Test
    void format() {
        // given
        String yyyyMMdd = "20241231";
        String HHmm = "0700";

        // when
        String format = DateConverter.format(yyyyMMdd, HHmm);

        // then
        assertThat(format).isEqualTo("202412310700");

    }

    @Test
    void localDateTimeFormat() {
        // given
        LocalDateTime compareDate = LocalDateTime.of(2024, 12, 31, 6, 0);

        // when
        String format = DateConverter.format(compareDate);

        // then
        assertThat(format).isEqualTo("2024-12-31 06:00");
    }

    @Test
    void convertDateTime() {
        // given
        String dateType1 = "202401010700";
        String dateType2 = "20240101";

        // when
        LocalDateTime dateTime1 = DateConverter.convertDateTime(dateType1);
        LocalDateTime dateTime2 = DateConverter.convertDateTime(dateType2);

        // then
        LocalDateTime expectedValue1 = LocalDateTime.of(2024, 1, 1, 7, 0);
        LocalDateTime expectedValue2 = LocalDateTime.of(2024, 1, 1, 0, 0);

        assertThat(dateTime1).isEqualTo(expectedValue1);
        assertThat(dateTime2).isEqualTo(expectedValue2);
    }

    @DisplayName("3일 이내의 데이터만 true 를 반환해야 한다.")
    @Test
    void isValidFormatDate() {
        // given
        LocalDateTime now = LocalDateTime.now();
        String minus1 = testConvert(now.minusDays(1));
        String minus2 = testConvert(now.minusDays(2));
        String minus3 = testConvert(now.minusDays(3));

        // when
        boolean valid1 = DateConverter.isValidFormatDate(minus1);
        boolean valid2 = DateConverter.isValidFormatDate(minus2);
        boolean valid3 = DateConverter.isValidFormatDate(minus3);

        // then
        assertThat(valid1).isTrue();
        assertThat(valid2).isTrue();
        assertThat(valid3).isTrue();
    }

    @DisplayName("3일 이내의 데이터가 아니면 false 를 반환해야 한다.")
    @Test
    void isValidFormatDate2() {
        // given
        LocalDateTime now = LocalDateTime.now();
        String minus1 = testConvert(now.minusDays(4));
        String minus2 = testConvert(now.minusDays(5));
        String plus1 = testConvert(now.plusDays(1));
        String plus2 = testConvert(now.plusDays(2));

        // when
        boolean valid1 = DateConverter.isValidFormatDate(minus1);
        boolean valid2 = DateConverter.isValidFormatDate(minus2);
        boolean valid3 = DateConverter.isValidFormatDate(plus1);
        boolean valid4 = DateConverter.isValidFormatDate(plus2);

        // then
        assertThat(valid1).isFalse();
        assertThat(valid2).isFalse();
        assertThat(valid3).isFalse();
        assertThat(valid4).isFalse();
    }

    private String testConvert(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return date.format(formatter);
    }
}