package module.apiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:application.properties", "classpath:application-internal.properties"})
@EntityScan("module")
@SpringBootApplication(scanBasePackages = {"module"})
public class ApiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiServerApplication.class, args);
    }

}
