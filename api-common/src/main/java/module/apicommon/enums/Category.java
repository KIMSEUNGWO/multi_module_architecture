package module.apicommon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {

    TMP("1시간 기온", "℃"),
    TMN("일 최저기온", "℃"),
    TMX("일 최고기온", "℃"),
    UUU("풍속(동서성분)", "m/s"),
    VVV("풍속(남북성분)", "m/s"),
    WAV("파고", "m"),
    VEC("풍향", "deg"),
    WSD("풍속", "m/s"),
    POP("강수확률", "%"),
    PTY("강수형태", ""),
    PCP("1시간 강수량", "mm"),
    REH("습도", "%"),
    SNO("1시간 신적설", ""),
    SKY("하늘상태", "");

    private final String content;
    private final String unit;

    public String combineValue(String dataValue) {
        if (dataValue.contains("없음")) {
            return String.format("%s %s", content, dataValue);
        }
        return String.format("%s %s%s", content, dataValue, unit);
    }
}
