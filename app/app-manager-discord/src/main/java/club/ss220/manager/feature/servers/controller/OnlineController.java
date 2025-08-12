package club.ss220.manager.feature.servers.controller;

import club.ss220.core.shared.GameServerData;
import club.ss220.core.application.GetAllServersStatusUseCase;
import club.ss220.core.config.GameConfig;
import club.ss220.core.shared.GameServerStatusData;
import club.ss220.manager.feature.servers.view.OnlineView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class OnlineController {

    private final OnlineView view;
    private final GetAllServersStatusUseCase getAllServersStatus;
    private final GameConfig gameConfig;

    public void showPlayersOnline(InteractionHook hook) {
        try {
            List<GameServerData> servers = gameConfig.getServers();
            Map<GameServerData, GameServerStatusData> serversStatuses = getAllServersStatus.execute(servers);
            view.renderPlayersOnline(hook, serversStatuses);

            log.debug("Displayed online players for {} servers", serversStatuses.size());
        } catch (Exception e) {
            throw new RuntimeException("Error displaying online players", e);
        }
    }
}
