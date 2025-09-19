package club.ss220.manager.feature.whitelist.command;

import club.ss220.manager.feature.whitelist.controller.WhitelistController;
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
public class WhitelistShowCommand extends ApplicationCommand {

    private final WhitelistController controller;

    @TopLevelSlashCommandData(defaultLocked = true)
    @JDASlashCommand(name = "whitelist", subcommand = "show", description = "Показать записи в белых списках серверов.")
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
        controller.show(event, playerTarget, adminTarget, serverType, onlyActive);
    }
}
