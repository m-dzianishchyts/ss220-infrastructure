package club.ss220.core.application;

import club.ss220.core.shared.GameBuild;
import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.GameServerStatusData;
import club.ss220.core.spi.GameServerPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class GetServerStatusUseCase {

    private final Map<GameBuild, GameServerPort> gameServerPorts;

    public GameServerStatusData execute(GameServerData server) {
        GameServerPort gameServerPort = gameServerPorts.get(server.build());
        return gameServerPort.getServerStatus(server);
    }
}
