package club.ss220.manager.feature.member.command;

import club.ss220.manager.feature.member.controller.MemberInfoController;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import lombok.AllArgsConstructor;

@Command
@AllArgsConstructor
public class MeCommand extends ApplicationCommand {

    private final MemberInfoController memberInfoController;

    @JDASlashCommand(name = "me", description = "Показать информацию о себе.")
    public void onSlashInteraction(GuildSlashEvent event) {
        boolean ephemeral = true;
        event.deferReply(ephemeral).queue();
        memberInfoController.showMemberInfo(event.getHook(), event.getUser());
    }
}
