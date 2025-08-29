package club.ss220.manager.feature.monitoring.server.command;

import club.ss220.core.shared.GameServerData;
import club.ss220.manager.feature.monitoring.server.controller.DebugController;
import club.ss220.manager.shared.ActiveGameServerData;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Command
@AllArgsConstructor
public class DebugCommand extends ApplicationCommand {

    private final DebugController debugController;

    @JDASlashCommand(name = "debug", description = "Отладочные данные от игрового сервера.")
    @TopLevelSlashCommandData(defaultLocked = true)
    public void onSlashInteraction(GuildSlashEvent event,
                                   @SlashOption(description = "Игровой сервер.", usePredefinedChoices = true)
                                   ActiveGameServerData server) {
        GameServerData activeServer = server.server();
        log.debug("Executing /debug command, server: {}", activeServer.fullName());

        boolean ephemeral = true;
        event.deferReply(ephemeral).queue();
        debugController.showServerDebugInfo(event.getHook(), activeServer);
    }
}
