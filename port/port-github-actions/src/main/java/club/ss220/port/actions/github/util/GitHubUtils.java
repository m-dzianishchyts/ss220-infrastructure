package club.ss220.port.actions.github.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHWorkflowRun;

import java.util.Set;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class GitHubUtils {

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
