package club.ss220.manager.feature.note.command;

import club.ss220.core.shared.GameServerData;
import club.ss220.manager.feature.note.controller.NotesController;
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
public class NotesCommand extends ApplicationCommand {

    private final NotesController notesController;

    @TopLevelSlashCommandData(defaultLocked = true)
    @JDASlashCommand(name = "notes", description = "Показать заметки об игроках.")
    public void onSlashInteractionNotes(GuildSlashEvent event,
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
                                        @SlashOption(description = "Активные (не удалённые).")
                                        Boolean active) {
        boolean ephemeral = true;
        event.deferReply(ephemeral).queue();
        notesController.showNotes(event.getHook(), playerTarget, adminTarget, server, roundId, active);
    }
}
