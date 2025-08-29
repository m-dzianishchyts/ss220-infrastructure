package club.ss220.core.application;

import club.ss220.core.shared.GameBuild;
import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.OnlineAdminStatusData;
import club.ss220.core.spi.GameServerPort;
import club.ss220.core.spi.exception.GameServerPortException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class GetOnlineAdminsListUseCase {

    private final Map<GameBuild, GameServerPort> gameServerPorts;

    public Map<GameServerData, List<OnlineAdminStatusData>> execute(List<GameServerData> servers) {
        if (servers == null || servers.isEmpty()) {
            return Map.of();
        }

        Map<GameServerData, List<OnlineAdminStatusData>> result = new HashMap<>();
        servers.stream().filter(GameServerData::active).forEach(server -> {
            GameServerPort gameServerPort = gameServerPorts.get(server.build());
            try {
                result.put(server, gameServerPort.getAdminsList(server));
            } catch (GameServerPortException e) {
                log.error("Error getting admins list for server: {}", server.fullName(), e);
            }
        });
        return result;
    }
}
