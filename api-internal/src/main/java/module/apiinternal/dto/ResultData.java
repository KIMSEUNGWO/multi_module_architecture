package module.apiinternal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Getter;

import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class ResultData {

    private String date;
    private List<String> detailData;


}
