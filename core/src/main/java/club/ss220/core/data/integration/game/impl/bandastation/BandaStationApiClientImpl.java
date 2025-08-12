package club.ss220.core.data.integration.game.impl.bandastation;

import club.ss220.core.config.GameConfig;
import club.ss220.core.data.integration.game.impl.AbstractGameApiClient;
import club.ss220.core.shared.GameServer;
import club.ss220.core.shared.GameServerStatus;
import club.ss220.core.shared.OnlineAdminStatus;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

@Slf4j
@Deprecated
@Qualifier(GameConfig.BUILD_BANDASTRATION)
public class BandaStationApiClientImpl extends AbstractGameApiClient {

    @Override
    public Mono<GameServerStatus> getServerStatus(GameServer gameServer) {
        return callServer(gameServer, "status", new TypeReference<BandaStationServerStatusDTO>() {})
                .map(Function.identity());
    }

    @Override
    public Mono<List<OnlineAdminStatus>> getAdminsList(GameServer gameServer) {
        return callServer(gameServer, "adminwho", new TypeReference<List<BandaStationOnlineAdminStatusDTO>>() {})
                .map(List::copyOf);
    }
}
