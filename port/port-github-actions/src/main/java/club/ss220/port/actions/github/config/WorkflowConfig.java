package club.ss220.port.actions.github.config;

import club.ss220.core.shared.GameBuild;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties("ss220.workflow")
public class WorkflowConfig {

    @Valid
    private Map<GameBuild, MergeWorkflowConfig> merge;
}
