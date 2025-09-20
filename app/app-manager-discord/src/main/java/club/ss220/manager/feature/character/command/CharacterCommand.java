package club.ss220.manager.feature.character.command;

import club.ss220.core.shared.GameBuild;
import club.ss220.manager.feature.character.controller.CharacterController;
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
public class CharacterCommand extends ApplicationCommand {

    private final CharacterController characterController;

    @JDASlashCommand(name = "character", description = "Узнать владельца персонажа.")
    @TopLevelSlashCommandData(defaultLocked = true)
    public void onSlashInteraction(GuildSlashEvent event,
                                   @Nullable
                                   @SlashOption(description = "Игровой билд.", usePredefinedChoices = true)
                                   GameBuild build,
                                   @SlashOption(description = "Имя персонажа или его часть.")
                                   String name) {
        characterController.searchCharacters(event, build, name);
    }
}
