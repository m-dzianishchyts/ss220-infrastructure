package club.ss220.manager.feature.whitelist.command;

import club.ss220.manager.feature.whitelist.controller.WhitelistController;
import club.ss220.manager.shared.GameServerType;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;

@Command
@RequiredArgsConstructor
public class WhitelistMeCommand extends ApplicationCommand {

    private final WhitelistController controller;

    @JDASlashCommand(name = "me", subcommand = "whitelist",
                     description = "Показать мои записи в белых списках серверов.")
    public void onSlashInteraction(GuildSlashEvent event,
                                   @Nullable
                                   @SlashOption(description = "Тип сервера.", usePredefinedChoices = true)
                                   GameServerType serverType,
                                   @Nullable
                                   @SlashOption(description = "Только активные.")
                                   Boolean onlyActive) {
        controller.showMine(event, event.getUser().getIdLong(), serverType, onlyActive);
    }
}
