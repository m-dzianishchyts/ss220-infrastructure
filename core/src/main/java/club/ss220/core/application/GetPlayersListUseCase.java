package club.ss220.core.application;

import club.ss220.core.shared.GameServerData;
import club.ss220.core.spi.GameServerPort;
import club.ss220.core.shared.GameBuild;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class GetPlayersListUseCase {

    private final Map<GameBuild, GameServerPort> gameServerPorts;

    public List<String> execute(GameServerData server) {
        GameServerPort gameServerPort = gameServerPorts.get(server.getBuild());
        return gameServerPort.getPlayersList(server);
    }
}
