package club.ss220.manager.feature.merge.controller;

import club.ss220.core.application.MergeUpstreamUseCase;
import club.ss220.core.shared.GameBuild;
import club.ss220.core.shared.WorkflowRunStatus;
import club.ss220.manager.feature.merge.view.MergeView;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MergeController {

    private final MergeUpstreamUseCase mergeUpstreamUseCase;
    private final MergeView view;

    public void execute(IReplyCallback interaction, GameBuild build, @Nullable Boolean translateChangelog) {
        interaction.deferReply().queue();

        boolean translateChangelogStrict = Optional.ofNullable(translateChangelog).orElse(true);
        mergeUpstreamUseCase.launch(build, translateChangelogStrict)
                .thenAccept(workflowRun -> {
                    log.debug("Accepting workflow dispatch result: {}", workflowRun);
                    handleWorkflowDispatch(interaction, build, workflowRun);
                });
    }

    private void handleWorkflowDispatch(IReplyCallback interaction, GameBuild build, WorkflowRunStatus workflowRun) {
        interaction.getHook().sendMessageEmbeds(view.renderDispatchSuccess(build, workflowRun))
                .queue(acknowledge -> {
                    log.debug("Displayed successful merge upstream acknowledgement.");

                    User initiator = interaction.getUser();
                    awaitWorkflowConclusion(build, initiator, acknowledge, workflowRun.id());
                });
    }

    private void awaitWorkflowConclusion(GameBuild build, User initiator, Message acknowledge, long workflowRunId) {
        mergeUpstreamUseCase.awaitConclusion(build, workflowRunId)
                .thenAccept(result -> {
                    log.debug("Accepting workflow conclusion result: {}", result);
                    handleWorkflowConclusion(build, initiator, acknowledge, result);
                })
                .exceptionally(throwable -> {
                    log.error("Failed to await workflow conclusion for run #{}", workflowRunId, throwable);
                    handleWorkflowError(build, initiator, acknowledge, throwable);
                    return null;
                });
    }

    private void handleWorkflowConclusion(GameBuild build, User initiator, Message acknowledge,
                                          WorkflowRunStatus result) {
        MessageEmbed embed;
        if (Boolean.TRUE.equals(result.success())) {
            embed = view.renderSuccess(build, result);
        } else if (Boolean.FALSE.equals(result.success())) {
            embed = view.renderFail(build, result);
        } else {
            embed = view.renderTimeout(build, result);
        }

        MessageCreateData message = new MessageCreateBuilder()
                .addContent(initiator.getAsMention())
                .addEmbeds(embed)
                .build();
        acknowledge.reply(message).queue();
        log.debug("Displayed merge upstream conclusion.");
    }

    private void handleWorkflowError(GameBuild build, User initiator, Message acknowledge, Throwable throwable) {
        MessageCreateData message = new MessageCreateBuilder()
                .addContent(initiator.getAsMention())
                .addEmbeds(view.renderError(build, throwable))
                .build();
        acknowledge.reply(message).queue();
        log.debug("Displayed merge upstream error.");
    }
}
