package club.ss220.manager.feature.member.command;

import club.ss220.manager.feature.member.controller.MemberInfoController;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import lombok.AllArgsConstructor;

@Command
@AllArgsConstructor
public class MeInfoCommand extends ApplicationCommand {

    private final MemberInfoController memberInfoController;

    @TopLevelSlashCommandData
    @JDASlashCommand(name = "me", subcommand = "info", description = "Показать информацию о себе.")
    public void onSlashInteraction(GuildSlashEvent event) {
        memberInfoController.renderMemberInfo(event, event.getUser());
    }
}
