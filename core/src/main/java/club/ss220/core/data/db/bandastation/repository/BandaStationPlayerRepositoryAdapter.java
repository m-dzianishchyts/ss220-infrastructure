package club.ss220.core.data.db.bandastation.repository;

import club.ss220.core.config.GameConfig;
import club.ss220.core.data.integration.game.PlayerRepositoryAdapter;
import club.ss220.core.data.mapper.Mappers;
import club.ss220.core.shared.Player;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import java.util.Optional;

@Deprecated
@ConditionalOnBean(Mappers.class)
@Qualifier(GameConfig.BUILD_BANDASTRATION)
public class BandaStationPlayerRepositoryAdapter implements PlayerRepositoryAdapter {

    private final BandaStationPlayerRepository repository;
    private final Mappers mappers;

    public BandaStationPlayerRepositoryAdapter(BandaStationPlayerRepository repository, Mappers mappers) {
        this.repository = repository;
        this.mappers = mappers;
    }

    @Override
    public Optional<Player> findByCkey(String ckey) {
        return repository.findByCkey(ckey).map(mappers::toPlayer);
    }
}
