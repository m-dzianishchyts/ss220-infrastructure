package club.ss220.manager.feature.whitelist.command;

import club.ss220.core.shared.GameServerType;
import club.ss220.manager.feature.whitelist.controller.WhitelistController;
import club.ss220.manager.shared.MemberTarget;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.LongRange;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;

@Command
@RequiredArgsConstructor
public class WhitelistAddCommand extends ApplicationCommand {

    private final WhitelistController controller;

    @JDASlashCommand(name = "whitelist", subcommand = "add", description = "Добавить игрока в белый список сервера.")
    public void onSlashInteraction(GuildSlashEvent event,
                                   @SlashOption(description = "Пользователь Discord/Discord ID/CKEY.")
                                   MemberTarget playerTarget,
                                   @SlashOption(description = "Тип сервера.", usePredefinedChoices = true)
                                   GameServerType serverType,
                                   @LongRange(from = 1, to = 9999)
                                   @SlashOption(description = "Срок действия (в днях).")
                                   int durationDays,
                                   @Nullable
                                   @SlashOption(description = "Игнорировать черный список.")
                                   Boolean ignoreBlacklist) {
        controller.addWhitelistEntry(event, playerTarget, event.getUser(), serverType, durationDays, ignoreBlacklist);
    }
}
