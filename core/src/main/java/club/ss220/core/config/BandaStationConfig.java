package club.ss220.core.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Deprecated
@Data
@Configuration("bandaStationConfigDeprecated")
@ConditionalOnMissingBean(name = "bandaStationConfig")
@ConfigurationProperties(prefix = "bandastation-deprecated")
public class BandaStationConfig {

    private Map<String, List<String>> roles;
}
