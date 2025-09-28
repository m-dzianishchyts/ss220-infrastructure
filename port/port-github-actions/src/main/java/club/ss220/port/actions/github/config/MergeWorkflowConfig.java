package club.ss220.port.actions.github.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.nio.file.Path;
import java.time.Duration;

public record MergeWorkflowConfig(
        @NotBlank
        String repoOwner,
        @NotBlank
        String repoName,
        @NotBlank
        String applicationId,
        @NotNull
        Path privateKeySource,
        @NotBlank
        String workflow,
        Duration timeout
) {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofMinutes(10);

    public MergeWorkflowConfig {
        if (timeout == null) {
            timeout = DEFAULT_TIMEOUT;
        }
    }
}
