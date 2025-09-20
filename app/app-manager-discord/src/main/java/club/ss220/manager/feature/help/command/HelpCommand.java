package club.ss220.manager.feature.help.command;

import club.ss220.manager.feature.help.controller.HelpController;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;

@Command
@RequiredArgsConstructor
public class HelpCommand extends ApplicationCommand {

    private final HelpController controller;

    @TopLevelSlashCommandData
    @JDASlashCommand(name = "help", description = "Показать инструкцию по использованию бота.")
    public void onSlashInteraction(GuildSlashEvent event,
                                   @Nullable
                                   @SlashOption(description = "Полное имя команды.")
                                   String command) {
        controller.showHelp(event, command);
    }
}
