package club.ss220.core.application;

import club.ss220.core.shared.GameBuild;
import club.ss220.core.shared.WorkflowRunStatus;
import club.ss220.core.spi.MergeUpstreamLauncher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MergeUpstreamUseCase {

    private final Map<GameBuild, MergeUpstreamLauncher> launchers;

    public CompletableFuture<WorkflowRunStatus> launch(GameBuild build, boolean translateChangelog) {
        MergeUpstreamLauncher launcher = requireLauncher(build);
        return launcher.launch(translateChangelog);
    }

    public CompletableFuture<WorkflowRunStatus> awaitConclusion(GameBuild build, long runId) {
        MergeUpstreamLauncher launcher = requireLauncher(build);
        return launcher.awaitConclusion(runId);
    }

    private MergeUpstreamLauncher requireLauncher(GameBuild build) {
        MergeUpstreamLauncher launcher = launchers.get(build);
        if (launcher == null) {
            String message = MergeUpstreamLauncher.class.getSimpleName() + " is not configured for build: " + build;
            throw new IllegalStateException(message);
        }
        return launcher;
    }
}
