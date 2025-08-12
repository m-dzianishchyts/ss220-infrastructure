package club.ss220.manager.feature.monitoring.server.controller;

import club.ss220.core.application.GetServerStatusUseCase;
import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.GameServerStatusData;
import club.ss220.manager.feature.monitoring.server.view.DebugView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class DebugController {

    private final DebugView view;
    private final GetServerStatusUseCase getServerStatus;

    public void showServerDebugInfo(InteractionHook hook, GameServerData server) {
        try {
            GameServerStatusData serverStatus = getServerStatus.execute(server);
            view.renderServerStatus(hook, server, serverStatus);

            log.debug("Displayed debug info for server {}", server.fullName());
        } catch (Exception e) {
            throw new RuntimeException("Error displaying server debug info for server " + server, e);
        }
    }
}
