package club.ss220.manager.feature.server.command;

import club.ss220.manager.feature.server.controller.OnlineController;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import lombok.AllArgsConstructor;

@Command
@AllArgsConstructor
public class OnlineCommand extends ApplicationCommand {

    private final OnlineController onlineController;

    @JDASlashCommand(name = "online", description = "Показать текущий онлайн.")
    public void onSlashInteraction(GuildSlashEvent event) {
        onlineController.showPlayersOnline(event);
    }
}
