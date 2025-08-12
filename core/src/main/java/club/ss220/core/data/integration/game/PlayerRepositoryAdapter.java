package club.ss220.core.data.integration.game;

import club.ss220.core.shared.Player;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Deprecated
@Repository
public interface PlayerRepositoryAdapter {

    Optional<Player> findByCkey(String ckey);
}
