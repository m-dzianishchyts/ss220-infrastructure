package club.ss220.manager.feature.monitoring.discord.controller;

import club.ss220.manager.feature.monitoring.discord.application.ApplicationStatusService;
import club.ss220.manager.feature.monitoring.discord.view.StatusView;
import club.ss220.manager.shared.ApplicationStatus;
import club.ss220.manager.shared.presentation.Senders;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class StatusController {

    private final StatusView view;
    private final ApplicationStatusService applicationStatusService;
    private final Senders senders;

    public void showApplicationStatus(IReplyCallback interaction, Guild guild) {
        ApplicationStatus applicationStatus = applicationStatusService.getApplicationStatus(guild);
        senders.sendEmbed(interaction, view.renderApplicationStatus(applicationStatus));
        log.debug("Displayed application status for guild {}", guild.getId());
    }
}
