package club.ss220.core.data.db;

import club.ss220.core.shared.GameCharacter;
import org.springframework.stereotype.Repository;

import java.util.List;

@Deprecated
@Repository
public interface CharacterRepositoryAdapter {

    List<GameCharacter> findByCkey(String ckey);

    List<GameCharacter> findByName(String name);
}
