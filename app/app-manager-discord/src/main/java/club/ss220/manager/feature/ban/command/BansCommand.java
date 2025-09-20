package club.ss220.manager.feature.ban.command;

import club.ss220.core.shared.BanData;
import club.ss220.core.shared.GameServerData;
import club.ss220.manager.feature.ban.controller.BansController;
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
public class BansCommand extends ApplicationCommand {

    private final BansController bansController;

    @TopLevelSlashCommandData(defaultLocked = true)
    @JDASlashCommand(name = "bans", description = "Показать блокировки.")
    public void onSlashInteractionPlayer(GuildSlashEvent event,
                                         @Nullable
                                         @SlashOption(description = "Пользователь Discord/Discord ID/CKEY.")
                                         MemberTarget playerTarget,
                                         @Nullable
                                         @SlashOption(description = "Пользователь Discord/Discord ID/CKEY.")
                                         MemberTarget adminTarget,
                                         @Nullable
                                         @SlashOption(description = "Игровой сервер.", usePredefinedChoices = true)
                                         GameServerData server,
                                         @Nullable
                                         @SlashOption(description = "ID раунда.")
                                         Integer roundId,
                                         @Nullable
                                         @SlashOption(description = "Перманентные.")
                                         Boolean permanent,
                                         @Nullable
                                         @SlashOption(description = "Тип блокировки.", usePredefinedChoices = true)
                                         BanData.BanType banType,
                                         @Nullable
                                         @SlashOption(description = "Истекшие.")
                                         Boolean expired,
                                         @Nullable
                                         @SlashOption(description = "Снятые.")
                                         Boolean unbanned) {
        bansController.showBans(event, playerTarget, adminTarget,
                                server, roundId, permanent, banType, expired, unbanned);
    }
}
