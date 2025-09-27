package club.ss220.storage.bandastation.spring.jpa.stub;

import club.ss220.core.config.GameConfig;
import club.ss220.core.shared.PlayerData;
import club.ss220.core.spi.PlayerStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Qualifier(GameConfig.BUILD_BANDASTRATION)
@ConditionalOnProperty(name = "spring.datasource.bandastation.url", havingValue = "false", matchIfMissing = true)
public class BandaStationPlayerStorageStub implements PlayerStorage {

    @Override
    public Optional<PlayerData> findByCkey(String ckey) {
        throw new IllegalStateException("Build 'bandastation' is disabled or datasource URL is not configured. Set 'spring.datasource.bandastation.url' or remove 'bandastation' from disabled builds.");
    }
}
