package club.ss220.storage.paradise.spring.jpa.player;

import club.ss220.storage.paradise.spring.jpa.player.mapper.ParadisePlayerMapper;
import club.ss220.storage.paradise.spring.jpa.player.repository.ParadisePlayerJpaRepository;
import club.ss220.core.shared.PlayerData;
import club.ss220.core.spi.PlayerStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static club.ss220.core.config.GameConfig.BUILD_PARADISE;

@Repository
@Qualifier(BUILD_PARADISE)
@RequiredArgsConstructor
public class ParadiseJpaPlayerStorage implements PlayerStorage {

    private final ParadisePlayerJpaRepository repository;
    private final ParadisePlayerMapper playerMapper;

    @Override
    public Optional<PlayerData> findByCkey(String ckey) {
        return repository.findByCkey(ckey).map(playerMapper::toPlayerData);
    }
}
