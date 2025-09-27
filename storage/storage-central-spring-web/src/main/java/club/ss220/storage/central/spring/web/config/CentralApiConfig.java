package club.ss220.storage.central.spring.web.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ss220.api.central")
public class CentralApiConfig {

    @NotBlank
    private String endpoint;
    @NotBlank
    private String token;
}
