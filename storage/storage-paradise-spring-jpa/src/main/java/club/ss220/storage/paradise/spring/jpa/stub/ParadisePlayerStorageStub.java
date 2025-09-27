package club.ss220.storage.paradise.spring.jpa.stub;

import club.ss220.core.shared.PlayerData;
import club.ss220.core.spi.PlayerStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static club.ss220.core.config.GameConfig.BUILD_PARADISE;

@Component
@Qualifier(BUILD_PARADISE)
@ConditionalOnProperty(name = "spring.datasource.paradise.url", havingValue = "false", matchIfMissing = true)
public class ParadisePlayerStorageStub implements PlayerStorage {

    @Override
    public Optional<PlayerData> findByCkey(String ckey) {
        throw new IllegalStateException("Build 'paradise' is disabled. Enable 'ss220.build.paradise.enabled=true' to use PlayerStorage for paradise.");
    }
}
