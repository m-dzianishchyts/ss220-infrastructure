package club.ss220.port.actions.github.mapper;

import club.ss220.core.shared.WorkflowRunStatus;
import jakarta.annotation.Nullable;
import org.kohsuke.github.GHWorkflowRun;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface WorkflowRunMapper {

    @Mapping(target = "success", source = "conclusion", qualifiedByName = "mapSuccess")
    WorkflowRunStatus toWorkflowRunStatus(GHWorkflowRun workflowRun);

    @Nullable
    @Named("mapSuccess")
    default Boolean mapSuccess(GHWorkflowRun.Conclusion conclusion) {
        if (conclusion == null) {
            return null;
        }
        return conclusion == GHWorkflowRun.Conclusion.SUCCESS;
    }
}
