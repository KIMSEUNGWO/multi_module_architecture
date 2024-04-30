package module.apiexternal.config;

import module.apiexternal.component.CustomTemplate;
import module.apiexternal.repository.ExternalConnector;
import module.apiexternal.repository.ExternalConnectorConn;
import module.apiexternal.service.ExternalService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;


@TestConfiguration
public class ExternalConfig {

    @Bean
    ExternalService externalService() {
        return new ExternalService(externalConnector());
    }

    @Bean
    ExternalConnector externalConnector() {
        return new ExternalConnectorConn(customTemplate());
    }

    @Bean
    CustomTemplate customTemplate() {
        return new CustomTemplate();
    }

}
