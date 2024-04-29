package module.apiexternal.config;

import module.apiexternal.component.CustomTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {

    @Bean
    CustomTemplate customTemplate() {
        return new CustomTemplate();
    }
}
