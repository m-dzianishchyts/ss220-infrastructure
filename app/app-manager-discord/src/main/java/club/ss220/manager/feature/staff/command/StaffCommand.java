package club.ss220.manager.feature.staff.command;

import club.ss220.manager.feature.staff.controller.StaffController;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Command
@AllArgsConstructor
public class StaffCommand extends ApplicationCommand {

    private final StaffController staffController;

    @JDASlashCommand(name = "staff", description = "Показать команду проекта онлайн.")
    public void onSlashInteraction(GuildSlashEvent event) {
        log.debug("Executing /staff command");

        boolean ephemeral = true;
        event.deferReply(ephemeral).queue();
        staffController.showOnlineStaff(event.getHook());
    }
}
