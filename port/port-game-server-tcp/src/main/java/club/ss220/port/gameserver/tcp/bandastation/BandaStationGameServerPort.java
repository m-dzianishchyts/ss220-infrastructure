package club.ss220.port.gameserver.tcp.bandastation;

import club.ss220.port.gameserver.tcp.bandastation.mapper.BandaStationOnlineAdminStatusMapper;
import club.ss220.port.gameserver.tcp.bandastation.mapper.BandaStationServerStatusMapper;
import club.ss220.port.gameserver.tcp.bandastation.presentation.BandaStationGameServerStatusPresentation;
import club.ss220.port.gameserver.tcp.bandastation.presentation.BandaStationOnlineAdminStatusPresentation;
import club.ss220.core.config.GameConfig;
import club.ss220.port.gameserver.tcp.support.AbstractTcpGameServerPort;
import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.GameServerStatusData;
import club.ss220.core.shared.OnlineAdminStatusData;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Qualifier(GameConfig.BUILD_BANDASTRATION)
@RequiredArgsConstructor
public class BandaStationGameServerPort extends AbstractTcpGameServerPort {

    public static final String PLAYER_LIST_COMMAND = "playerlist";
    private static final String SERVER_STATUS_COMMAND = "status";
    private static final String ADMIN_LIST_COMMAND = "adminwho";

    private final BandaStationServerStatusMapper statusMapper;
    private final BandaStationOnlineAdminStatusMapper onlineAdminStatusMapper;

    @Override
    public List<String> getPlayersList(GameServerData gameServer) {
        return callServer(gameServer, PLAYER_LIST_COMMAND, new TypeReference<>() {});
    }

    @Override
    public GameServerStatusData getServerStatus(GameServerData gameServer) {
        TypeReference<BandaStationGameServerStatusPresentation> typeRef = new TypeReference<>() { };
        var presentation = callServer(gameServer, SERVER_STATUS_COMMAND, typeRef);
        return statusMapper.mapToGameServerStatusData(presentation);
    }

    @Override
    public List<OnlineAdminStatusData> getAdminsList(GameServerData gameServer) {
        TypeReference<List<BandaStationOnlineAdminStatusPresentation>> typeRef = new TypeReference<>() { };
        var presentation = callServer(gameServer, ADMIN_LIST_COMMAND, typeRef);
        return presentation.stream()
                .map(onlineAdminStatusMapper::mapToOnlineAdminStatusData)
                .toList();
    }
}
