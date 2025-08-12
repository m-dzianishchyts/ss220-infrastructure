package club.ss220.port.gameserver.tcp.paradise;

import club.ss220.port.gameserver.tcp.paradise.mapper.ParadiseOnlineAdminStatusMapper;
import club.ss220.port.gameserver.tcp.paradise.mapper.ParadiseServerStatusMapper;
import club.ss220.port.gameserver.tcp.paradise.presentation.ParadiseGameServerStatusPresentation;
import club.ss220.port.gameserver.tcp.paradise.presentation.ParadiseOnlineAdminStatusPresentation;
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
@Qualifier(GameConfig.BUILD_PARADISE)
@RequiredArgsConstructor
public class ParadiseGameServerPort extends AbstractTcpGameServerPort {

    public static final String PLAYER_LIST_COMMAND = "playerlist";
    public static final String SERVER_STATUS_COMMAND = "status";
    public static final String ADMIN_LIST_COMMAND = "adminwho";

    private final ParadiseServerStatusMapper statusMapper;
    private final ParadiseOnlineAdminStatusMapper onlineAdminStatusMapper;

    @Override
    public List<String> getPlayersList(GameServerData gameServer) {
        return callServer(gameServer, PLAYER_LIST_COMMAND, new TypeReference<>() { });
    }

    @Override
    public GameServerStatusData getServerStatus(GameServerData gameServer) {
        TypeReference<ParadiseGameServerStatusPresentation> typeRef = new TypeReference<>() { };
        var presentation = callServer(gameServer, SERVER_STATUS_COMMAND, typeRef);
        return statusMapper.mapToGameServerStatusData(presentation);
    }

    @Override
    public List<OnlineAdminStatusData> getAdminsList(GameServerData gameServer) {
        TypeReference<List<ParadiseOnlineAdminStatusPresentation>> typeRef = new TypeReference<>() { };
        var presentation = callServer(gameServer, ADMIN_LIST_COMMAND, typeRef);
        return presentation.stream()
                .map(onlineAdminStatusMapper::mapToOnlineAdminStatusData)
                .toList();
    }
}
