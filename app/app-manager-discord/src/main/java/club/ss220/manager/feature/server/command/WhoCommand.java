package club.ss220.manager.feature.server.command;

import club.ss220.core.shared.GameServerData;
import club.ss220.manager.feature.server.controller.WhoController;
import club.ss220.manager.shared.ActiveGameServerData;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import lombok.AllArgsConstructor;

@Command
@AllArgsConstructor
public class WhoCommand extends ApplicationCommand {

    private final WhoController whoController;

    @JDASlashCommand(name = "who", description = "Показать список игроков на сервере.")
    public void onSlashInteraction(GuildSlashEvent event,
                                   @SlashOption(description = "Игровой сервер.", usePredefinedChoices = true)
                                   ActiveGameServerData server) {
        GameServerData activeServer = server.server();
        whoController.showPlayersOnServer(event, activeServer);
    }
}
