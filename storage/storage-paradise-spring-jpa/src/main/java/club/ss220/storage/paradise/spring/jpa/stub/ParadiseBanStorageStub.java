package club.ss220.storage.paradise.spring.jpa.stub;

import club.ss220.core.shared.BanData;
import club.ss220.core.spi.BanQuery;
import club.ss220.core.spi.BanStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static club.ss220.core.config.GameConfig.BUILD_PARADISE;

@Component
@Qualifier(BUILD_PARADISE)
@ConditionalOnProperty(name = "spring.datasource.paradise.url", havingValue = "false", matchIfMissing = true)
public class ParadiseBanStorageStub implements BanStorage {

    @Override
    public Optional<BanData> findById(Integer id) {
        throw new IllegalStateException("Build 'paradise' is disabled. Remove it from 'ss220.builds.disabled' or set 'ss220.build.paradise.disabled=false' to use BanStorage for paradise.");
    }

    @Override
    public List<BanData> findByQuery(BanQuery query) {
        throw new IllegalStateException("Build 'paradise' is disabled. Remove it from 'ss220.builds.disabled' or set 'ss220.build.paradise.disabled=false' to use BanStorage for paradise.");
    }
}
