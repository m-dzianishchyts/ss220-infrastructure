package club.ss220.core.spi;

import club.ss220.core.shared.PlayerData;

import java.util.Optional;

public interface PlayerStorage {

    Optional<PlayerData> findByCkey(String ckey);
}
