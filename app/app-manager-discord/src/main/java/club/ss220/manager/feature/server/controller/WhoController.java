package club.ss220.manager.feature.server.controller;

import club.ss220.core.application.GetOnlinePlayersListUseCase;
import club.ss220.core.shared.GameServerData;
import club.ss220.manager.feature.server.view.WhoView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class WhoController {

    private final WhoView view;
    private final GetOnlinePlayersListUseCase getPlayersList;

    public void showPlayersOnServer(InteractionHook hook, GameServerData server) {
        try {
            List<String> playersOnline = getPlayersList.execute(server);
            view.renderPlayersOnline(hook, server, playersOnline);
            
            log.debug("Displayed {} players on server {}", playersOnline.size(), server.fullName());
        } catch (Exception e) {
            throw new RuntimeException("Error displaying players list for server " + server, e);
        }
    }
}
