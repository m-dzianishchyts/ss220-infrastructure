package club.ss220.core.spi;

import club.ss220.core.shared.WorkflowRunStatus;

import java.util.concurrent.CompletableFuture;

public interface MergeUpstreamLauncher {

    CompletableFuture<WorkflowRunStatus> launch(boolean translateChangelog);

    CompletableFuture<WorkflowRunStatus> awaitConclusion(long workflowRunId);
}
