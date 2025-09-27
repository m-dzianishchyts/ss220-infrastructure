package club.ss220.storage.bandastation.spring.jpa.stub;

import club.ss220.core.config.GameConfig;
import club.ss220.core.shared.BanData;
import club.ss220.core.spi.BanQuery;
import club.ss220.core.spi.BanStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Qualifier(GameConfig.BUILD_BANDASTRATION)
@ConditionalOnProperty(name = "spring.datasource.bandastation.url", havingValue = "false", matchIfMissing = true)
public class BandaStationBanStorageStub implements BanStorage {

    @Override
    public Optional<BanData> findById(Integer id) {
        throw new IllegalStateException("Build 'bandastation' is disabled or datasource URL is not configured. Set 'spring.datasource.bandastation.url' or remove 'bandastation' from disabled builds.");
    }

    @Override
    public List<BanData> findByQuery(BanQuery query) {
        throw new IllegalStateException("Build 'bandastation' is disabled or datasource URL is not configured. Set 'spring.datasource.bandastation.url' or remove 'bandastation' from disabled builds.");
    }
}
