package club.ss220.manager.feature.member.command;

import club.ss220.manager.feature.member.controller.MemberInfoController;
import club.ss220.manager.shared.MemberTarget;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.CommandScope;
import io.github.freya022.botcommands.api.commands.application.context.annotations.JDAUserCommand;
import io.github.freya022.botcommands.api.commands.application.context.user.GuildUserEvent;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import lombok.AllArgsConstructor;

@Command
@AllArgsConstructor
public class PlayerInfoCommand extends ApplicationCommand {

    private final MemberInfoController memberInfoController;

    @JDASlashCommand(name = "player", description = "Получить информацию об игроке.")
    @TopLevelSlashCommandData(defaultLocked = true)
    public void onSlashInteraction(GuildSlashEvent event,
                                   @SlashOption(description = "Пользователь Discord/Discord ID/CKEY.")
                                   MemberTarget target) {
        boolean ephemeral = true;
        event.deferReply(ephemeral).queue();
        memberInfoController.showMemberInfo(event.getHook(), event.getUser(), target);
    }

    @JDAUserCommand(name = "Информация об игроке", scope = CommandScope.GUILD)
    public void onUserInteraction(GuildUserEvent event) {
        boolean ephemeral = true;
        event.deferReply(ephemeral).queue();
        memberInfoController.showMemberInfo(event.getHook(), event.getUser(), event.getTarget());
    }
}
