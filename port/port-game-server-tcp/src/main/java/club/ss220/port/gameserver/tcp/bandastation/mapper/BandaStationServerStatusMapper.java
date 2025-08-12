package club.ss220.port.gameserver.tcp.bandastation.mapper;

import club.ss220.port.gameserver.tcp.bandastation.presentation.BandaStationGameServerStatusPresentation;
import club.ss220.core.shared.GameServerStatusData;
import org.springframework.stereotype.Component;

@Component
public class BandaStationServerStatusMapper {

    public GameServerStatusData mapToGameServerStatusData(BandaStationGameServerStatusPresentation presentation) {
        return GameServerStatusData.builder()
                .players(presentation.players())
                .admins(presentation.admins())
                .roundDuration(presentation.roundDuration())
                .rawData(presentation.rawData())
                .build();
    }
}
