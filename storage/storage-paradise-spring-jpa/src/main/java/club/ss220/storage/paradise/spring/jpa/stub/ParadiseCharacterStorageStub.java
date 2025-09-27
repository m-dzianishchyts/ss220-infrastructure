package club.ss220.storage.paradise.spring.jpa.stub;

import club.ss220.core.shared.GameCharacterData;
import club.ss220.core.spi.CharacterQuery;
import club.ss220.core.spi.CharacterStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

import static club.ss220.core.config.GameConfig.BUILD_PARADISE;

@Component
@Qualifier(BUILD_PARADISE)
@ConditionalOnProperty(name = "spring.datasource.paradise.url", havingValue = "false", matchIfMissing = true)
public class ParadiseCharacterStorageStub implements CharacterStorage {

    @Override
    public List<GameCharacterData> findByQuery(CharacterQuery query) {
        throw new IllegalStateException("Build 'paradise' is disabled. Enable 'ss220.build.paradise.enabled=true' to use CharacterStorage for paradise.");
    }
}
