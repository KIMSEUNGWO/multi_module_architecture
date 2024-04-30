package module.apiexternal.mockentity;

import module.apicommon.enums.Category;
import module.apicommon.enums.ErrorCode;
import module.apiexternal.json.*;

import java.util.ArrayList;
import java.util.List;

public class MockJsonData {

    public static MockJsonBuilder createJsonData() {
        JsonData data = new JsonData();
        JsonResponse response = new JsonResponse();
        JsonHeader header = new JsonHeader();

        response.setHeader(header);
        data.setResponse(response);

        return new MockJsonBuilder(data);
    }

    public static class MockJsonBuilder {

        private final JsonData jsonData;

        public MockJsonBuilder(JsonData jsonData) {
            this.jsonData = jsonData;
        }

        public MockJsonBuilder setErrorCode(String code) {
            JsonResponse response = jsonData.getResponse();
            JsonHeader header = response.getHeader();
            header.setResultCode(code);
            header.setResultMsg("Example Message");
            return this;
        }

        public MockJsonBuilder setBody() {
            JsonResponse response = jsonData.getResponse();

            JsonBody body = new JsonBody();

            body.setDataType("JSON");
            body.setPageNo(1);
            body.setNumOfRows(12);
            body.setTotalCount(12);


            JsonItems jsonItems = new JsonItems();
            jsonItems.setItem(new ArrayList<>());
            body.setItems(jsonItems);

            response.setBody(body);
            return this;
        }

        public MockJsonBuilder setJsonItem() {
            List<JsonItem> itemList = jsonItemListInit();

            JsonItem jsonItem = new JsonItem();
            jsonItem.setFcstDate("20240101");
            jsonItem.setFcstTime("0600");
            jsonItem.setCategory(Category.TMP.name());
            jsonItem.setFcstValue("20");
            itemList.add(jsonItem);

            return this;
        }

        public MockJsonBuilder setJsonItem(String fcstDate, String fcstTime, Category category, String fcstValue) {
            List<JsonItem> itemList = jsonItemListInit();

            JsonItem jsonItem = new JsonItem();
            jsonItem.setFcstDate(fcstDate);
            jsonItem.setFcstTime(fcstTime);
            jsonItem.setCategory(category.name());
            jsonItem.setFcstValue(fcstValue);
            itemList.add(jsonItem);

            return this;
        }

        private List<JsonItem> jsonItemListInit() {
            JsonItems items = jsonData.getResponse().getBody().getItems();
            if (items == null) setBody();
            return jsonData.getItemList();
        }

        public JsonData build() {
            return jsonData;
        }
    }
}
