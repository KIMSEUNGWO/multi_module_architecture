package module.apiexternal.json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class JsonItems {

    private List<JsonItem> item;

}
