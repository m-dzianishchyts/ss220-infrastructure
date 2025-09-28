package club.ss220.manager.config;

import club.ss220.core.config.GameConfig;
import club.ss220.core.shared.GameBuild;
import club.ss220.core.spi.MergeUpstreamLauncher;
import club.ss220.port.actions.github.GithubMergeUpstreamLauncher;
import club.ss220.port.actions.github.config.WorkflowConfig;
import club.ss220.port.actions.github.mapper.WorkflowRunMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class WorkflowLauncherConfig {

    private final GameConfig gameConfig;

    @Bean
    public Map<GameBuild, MergeUpstreamLauncher> mergeUpstreamLaunchers(WorkflowConfig workflowConfig,
                                                                        WorkflowRunMapper workflowRunMapper) {
        return workflowConfig.getMerge().entrySet().stream()
                .filter(entry -> gameConfig.getBuilds().get(entry.getKey()).isEnabled())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new GithubMergeUpstreamLauncher(entry.getValue(), workflowRunMapper)
                ));
    }
}
