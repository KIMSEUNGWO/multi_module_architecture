package module.apiexternal;

import java.net.URISyntaxException;

public interface ExternalConnector {

    <T> T getJsonData(String date, Class<T> clazz) throws URISyntaxException;
}
