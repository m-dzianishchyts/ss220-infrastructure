package club.ss220.manager.feature.server.controller;

import club.ss220.core.application.GetOnlinePlayersListUseCase;
import club.ss220.core.shared.GameServerData;
import club.ss220.manager.feature.server.view.WhoView;
import club.ss220.manager.shared.presentation.Senders;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class WhoController {

    private final WhoView view;
    private final GetOnlinePlayersListUseCase getPlayersList;
    private final Senders senders;

    public void showPlayersOnServer(IReplyCallback interaction, GameServerData server) {
        List<String> playersOnline = getPlayersList.execute(server);
        senders.sendEmbedEphemeral(interaction, view.renderPlayersOnline(server, playersOnline));
        log.debug("Displayed {} players on server {}", playersOnline.size(), server.fullName());
    }
}
