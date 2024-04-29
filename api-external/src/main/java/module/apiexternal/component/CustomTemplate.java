package module.apiexternal.component;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class CustomTemplate {

    private final RestTemplate restTemplate;
    private final HttpEntity<?> entity;

    public CustomTemplate() {
        this.restTemplate = new RestTemplate();
        this.entity = new HttpEntity<>(getHeaders());
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "*/*;q=0.9"); // HTTP_ERROR 방지
        return headers;
    }

    public <T> T get(URI uri, Class<T> clazz) {
        return restTemplate.exchange(uri, HttpMethod.GET, entity, clazz).getBody();
    }


}
