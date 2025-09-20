package club.ss220.manager.feature.monitoring.discord.command;

import club.ss220.manager.feature.monitoring.discord.controller.StatusController;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import lombok.AllArgsConstructor;

@Command
@AllArgsConstructor
public class StatusCommand extends ApplicationCommand {

    private final StatusController statusController;

    @JDASlashCommand(name = "status", description = "Показать статус бота.")
    @TopLevelSlashCommandData(defaultLocked = true)
    public void onSlashInteraction(GuildSlashEvent event) {
        statusController.showApplicationStatus(event, event.getGuild());
    }
}
