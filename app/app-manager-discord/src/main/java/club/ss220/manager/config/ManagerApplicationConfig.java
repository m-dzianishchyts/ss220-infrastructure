package club.ss220.manager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "application.discord")
public class ManagerApplicationConfig {

    private String token;
    private String status;
    private ProfileConfig profile;

    public record ProfileConfig(
            String nickname,
            @DefaultValue("true")
            boolean autoUpdate
    ) { }
}
