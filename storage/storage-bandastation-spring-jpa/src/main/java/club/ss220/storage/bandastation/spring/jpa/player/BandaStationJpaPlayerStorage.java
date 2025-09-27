package club.ss220.storage.bandastation.spring.jpa.player;

import club.ss220.core.config.GameConfig;
import club.ss220.core.shared.PlayerData;
import club.ss220.core.spi.PlayerStorage;
import club.ss220.storage.bandastation.spring.jpa.player.mapper.BandaStationPlayerMapper;
import club.ss220.storage.bandastation.spring.jpa.player.repository.BandaStationPlayerJpaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Qualifier(GameConfig.BUILD_BANDASTRATION)
@ConditionalOnProperty(name = "spring.datasource.bandastation.url")
public class BandaStationJpaPlayerStorage implements PlayerStorage {

    private final BandaStationPlayerJpaRepository repository;
    private final BandaStationPlayerMapper mapper;

    public BandaStationJpaPlayerStorage(BandaStationPlayerJpaRepository repository, BandaStationPlayerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<PlayerData> findByCkey(String ckey) {
        return repository.findByCkey(ckey).map(mapper::toPlayerData);
    }
}
