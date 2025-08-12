package club.ss220.core.spi;

import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.GameServerStatusData;
import club.ss220.core.shared.OnlineAdminStatusData;

import java.util.List;

public interface GameServerPort {

    GameServerStatusData getServerStatus(GameServerData gameServer);

    List<String> getPlayersList(GameServerData gameServer);

    List<OnlineAdminStatusData> getAdminsList(GameServerData gameServer);
}
