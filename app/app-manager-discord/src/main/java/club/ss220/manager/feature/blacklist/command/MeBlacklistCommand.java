package club.ss220.manager.feature.blacklist.command;

import club.ss220.manager.feature.blacklist.controller.BlacklistController;
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
public class MeBlacklistCommand extends ApplicationCommand {

    private final BlacklistController controller;

    @JDASlashCommand(name = "me", subcommand = "blacklist",
                     description = "Показать мои записи в черных списках серверов.")
    public void onSlashInteraction(GuildSlashEvent event,
                                   @Nullable
                                   @SlashOption(description = "Тип сервера.", usePredefinedChoices = true)
                                   GameServerType serverType,
                                   @Nullable
                                   @SlashOption(description = "Только активные.")
                                   Boolean activeOnly) {
        controller.showUserBlacklist(event, event.getUser().getIdLong(), serverType, activeOnly);
    }
}
