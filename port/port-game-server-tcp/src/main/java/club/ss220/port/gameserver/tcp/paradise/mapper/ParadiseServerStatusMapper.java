package club.ss220.port.gameserver.tcp.paradise.mapper;

import club.ss220.port.gameserver.tcp.paradise.presentation.ParadiseGameServerStatusPresentation;
import club.ss220.core.shared.GameServerStatusData;
import org.springframework.stereotype.Component;

@Component
public class ParadiseServerStatusMapper {

    public GameServerStatusData mapToGameServerStatusData(ParadiseGameServerStatusPresentation presentation) {
        return GameServerStatusData.builder()
                .players(presentation.players())
                .admins(presentation.admins())
                .roundDuration(presentation.roundDuration())
                .rawData(presentation.rawData())
                .build();
    }
}
