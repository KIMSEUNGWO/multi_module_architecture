package module.apiexternal.json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class JsonBody {

    private String dataType;
    private JsonItems items;
    private int pageNo;
    private int numOfRows;
    private int totalCount;

    public List<JsonItem> getItemList() {
        return items.getItem();
    }
}
