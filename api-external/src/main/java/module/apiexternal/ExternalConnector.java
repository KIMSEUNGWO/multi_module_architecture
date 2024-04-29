package module.apiexternal;

import module.apiexternal.json.JsonData;

import java.net.URISyntaxException;

public interface ExternalConnector {

    JsonData getJsonData(String date) throws URISyntaxException;
}
