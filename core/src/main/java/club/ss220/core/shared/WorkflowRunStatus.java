package club.ss220.core.shared;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Builder;

@Builder
public record WorkflowRunStatus(
        long id,
        @Nullable
        String status,
        @Nullable
        String conclusion,
        @Nullable
        Boolean success,
        @Nonnull
        String htmlUrl
) {
}
