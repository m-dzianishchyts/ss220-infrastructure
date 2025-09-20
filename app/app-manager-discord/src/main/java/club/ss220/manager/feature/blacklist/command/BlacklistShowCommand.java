package club.ss220.manager.feature.blacklist.command;

import club.ss220.manager.feature.blacklist.controller.BlacklistController;
import club.ss220.manager.shared.GameServerType;
import club.ss220.manager.shared.MemberTarget;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;

@Command
@AllArgsConstructor
public class BlacklistShowCommand extends ApplicationCommand {

    private final BlacklistController controller;

    @TopLevelSlashCommandData(defaultLocked = true)
    @JDASlashCommand(name = "blacklist", subcommand = "show",
                     description = "Показать записи в черных списках серверов.")
    public void onSlashInteraction(GuildSlashEvent event,
                                   @Nullable
                                   @SlashOption(description = "Пользователь Discord/Discord ID/CKEY.")
                                   MemberTarget playerTarget,
                                   @Nullable
                                   @SlashOption(description = "Пользователь Discord/Discord ID/CKEY.")
                                   MemberTarget adminTarget,
                                   @Nullable
                                   @SlashOption(description = "Тип сервера.", usePredefinedChoices = true)
                                   GameServerType serverType,
                                   @Nullable
                                   @SlashOption(description = "Только активные.")
                                   Boolean onlyActive) {
        controller.showBlacklist(event, playerTarget, adminTarget, serverType, onlyActive);
    }
}
