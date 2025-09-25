package club.ss220.manager.feature.monitoring.server.controller;

import club.ss220.core.application.GetServerStatusUseCase;
import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.GameServerStatusData;
import club.ss220.manager.feature.monitoring.server.view.DebugView;
import club.ss220.manager.shared.presentation.Senders;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class DebugController {

    private final DebugView view;
    private final GetServerStatusUseCase getServerStatus;
    private final Senders senders;

    public void showServerDebugInfo(IReplyCallback interaction, GameServerData server) {
        GameServerStatusData serverStatus = getServerStatus.execute(server);
        senders.sendEmbed(interaction, view.renderServerStatus(server, serverStatus));
        log.debug("Displayed debug info for server {}", server.id());
    }
}
