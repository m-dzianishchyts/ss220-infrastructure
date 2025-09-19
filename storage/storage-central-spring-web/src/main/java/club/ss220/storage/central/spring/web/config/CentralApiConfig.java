package club.ss220.storage.central.spring.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "application.api.central")
public class CentralApiConfig {

    private String endpoint;
    private String token;
}
