package club.ss220.manager.shared.event.listener;

import club.ss220.core.shared.GameServerType;
import club.ss220.core.shared.event.WhitelistUpdateEvent;
import club.ss220.manager.shared.application.RoleAssignmentService;
import io.github.freya022.botcommands.api.core.annotations.BEventListener;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WhitelistRoleAssignmentEventListener {

    @Nullable
    private JDA jda;
    private final RoleAssignmentService roleAssignmentService;

    @BEventListener
    public void onReady(ReadyEvent event) {
        jda = event.getJDA();
    }

    @EventListener
    public void onWhitelistUpdated(WhitelistUpdateEvent event) {
        if (jda == null) {
            throw new IllegalStateException("JDA is not ready, cannot assign whitelist role");
        }
        Guild guild = Objects.requireNonNull(jda.getGuildById(event.guildId()));
        long userDiscordId = event.userDiscordId();
        GameServerType serverType = event.serverType();

        switch (event.action()) {
            case ADD -> roleAssignmentService.addWhitelistRole(guild, userDiscordId, serverType);
            case REMOVE -> roleAssignmentService.removeWhitelistRole(guild, userDiscordId, serverType);
        }
    }
}
