package club.ss220.manager.shared.events.listener;

import club.ss220.manager.shared.GameServerType;
import club.ss220.manager.shared.application.RoleAssignmentService;
import club.ss220.manager.shared.events.WhitelistUpdateEvent;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WhitelistRoleAssignmentEventListener {

    private final RoleAssignmentService roleAssignmentService;

    @EventListener
    public void onWhitelistUpdated(WhitelistUpdateEvent event) {
        Guild guild = event.guild();
        long userDiscordId = event.userDiscordId();
        GameServerType serverType = event.serverType();

        switch (event.action()) {
            case ADD -> roleAssignmentService.addWhitelistRole(guild, userDiscordId, serverType);
            case REMOVE -> roleAssignmentService.removeWhitelistRole(guild, userDiscordId, serverType);
        }
    }
}
