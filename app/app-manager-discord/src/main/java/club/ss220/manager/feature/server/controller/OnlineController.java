package club.ss220.manager.feature.server.controller;

import club.ss220.core.application.GetAllServersStatusUseCase;
import club.ss220.core.config.GameConfig;
import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.GameServerStatusData;
import club.ss220.manager.feature.server.view.OnlineView;
import club.ss220.manager.shared.presentation.Senders;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
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
    private final Senders senders;

    public void showPlayersOnline(IReplyCallback interaction) {
        interaction.deferReply().queue();

        List<GameServerData> servers = gameConfig.getSupportedServers();
        Map<GameServerData, GameServerStatusData> serversStatuses = getAllServersStatus.execute(servers);
        senders.sendEmbed(interaction, view.renderPlayersOnline(serversStatuses));
        log.debug("Displayed online players for {} servers", serversStatuses.size());
    }
}
