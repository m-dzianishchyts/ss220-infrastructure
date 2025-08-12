package club.ss220.storage.bandastation.spring.jpa.player;

import club.ss220.storage.bandastation.spring.jpa.player.mapper.BandaStationPlayerMapper;
import club.ss220.storage.bandastation.spring.jpa.player.repository.BandaStationPlayerJpaRepository;
import club.ss220.core.config.GameConfig;
import club.ss220.core.shared.PlayerData;
import club.ss220.core.spi.PlayerStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Qualifier(GameConfig.BUILD_BANDASTRATION)
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
