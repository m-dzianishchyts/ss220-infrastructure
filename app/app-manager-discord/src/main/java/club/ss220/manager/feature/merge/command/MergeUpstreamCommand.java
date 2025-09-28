package club.ss220.manager.feature.merge.command;

import club.ss220.core.shared.GameBuild;
import club.ss220.manager.feature.merge.controller.MergeController;
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
public class MergeUpstreamCommand extends ApplicationCommand {

    private final MergeController controller;

    @TopLevelSlashCommandData(defaultLocked = true)
    @JDASlashCommand(name = "merge", description = "Инициировать мерж апстрима.")
    public void onSlashInteraction(GuildSlashEvent event,
                                   @SlashOption(description = "Игровой билд.", usePredefinedChoices = true)
                                   GameBuild build,
                                   @Nullable
                                   @SlashOption(description = "Перевести чейнджлог.")
                                   Boolean translateCl) {
        controller.execute(event, build, translateCl);
    }
}
