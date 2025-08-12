package club.ss220.core.spi;

import club.ss220.core.shared.GameCharacterData;

import java.util.List;

public interface CharacterStorage {

    List<GameCharacterData> findByCkey(String ckey);

    List<GameCharacterData> findByName(String name);
}
