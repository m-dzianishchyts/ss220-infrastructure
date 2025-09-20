package club.ss220.manager.feature.blacklist.command;

import club.ss220.manager.feature.blacklist.controller.BlacklistController;
import club.ss220.manager.shared.GameServerType;
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
public class BlacklistAddCommand extends ApplicationCommand {

    private final BlacklistController controller;

    @JDASlashCommand(name = "blacklist", subcommand = "add", description = "Добавить игрока в черный список сервера.")
    public void onSlashInteraction(GuildSlashEvent event,
                                   @SlashOption(description = "Пользователь Discord/Discord ID/CKEY.")
                                   MemberTarget playerTarget,
                                   @SlashOption(description = "Тип сервера.", usePredefinedChoices = true)
                                   GameServerType serverType,
                                   @LongRange(from = 1, to = 9999)
                                   @SlashOption(description = "Срок действия (в днях).")
                                   int durationDays,
                                   @SlashOption(description = "Причина.")
                                   String reason,
                                   @Nullable
                                   @SlashOption(description = "Удалить из белого списка.")
                                   Boolean removeWhitelist) {
        controller.addBlacklistEntry(event, playerTarget, event.getUser(), serverType,
                                     durationDays, reason, removeWhitelist);
    }
}
