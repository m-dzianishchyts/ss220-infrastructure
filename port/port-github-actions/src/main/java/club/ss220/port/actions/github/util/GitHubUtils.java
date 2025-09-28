package club.ss220.port.actions.github.util;

import org.kohsuke.github.GHWorkflowRun;

import java.util.Set;

public final class GitHubUtils {

    private GitHubUtils() {
    }

    public static boolean isWorkflowCompleted(GHWorkflowRun workflowRun) {
        return Set.of(
                GHWorkflowRun.Status.COMPLETED,
                GHWorkflowRun.Status.SUCCESS,
                GHWorkflowRun.Status.FAILURE,
                GHWorkflowRun.Status.CANCELLED,
                GHWorkflowRun.Status.TIMED_OUT,
                GHWorkflowRun.Status.SKIPPED,
                GHWorkflowRun.Status.NEUTRAL
        ).contains(workflowRun.getStatus());
    }
}
