package club.ss220.storage.paradise.spring.jpa.character;

import club.ss220.core.shared.GameCharacterData;
import club.ss220.core.spi.CharacterStorage;
import club.ss220.storage.paradise.spring.jpa.character.mapper.ParadiseCharacterMapper;
import club.ss220.storage.paradise.spring.jpa.character.repository.ParadiseCharacterJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

import static club.ss220.core.config.GameConfig.BUILD_PARADISE;

@Repository
@Qualifier(BUILD_PARADISE)
@RequiredArgsConstructor
public class ParadiseJpaCharacterStorage implements CharacterStorage {

    private final ParadiseCharacterJpaRepository repository;
    private final ParadiseCharacterMapper characterMapper;

    @Override
    public List<GameCharacterData> findByCkey(String ckey) {
        return repository.findByCkey(ckey).stream().map(characterMapper::toCharacterData).toList();
    }

    @Override
    public List<GameCharacterData> findByName(String name) {
        return repository.findByRealName(name).stream().map(characterMapper::toCharacterData).toList();
    }
}
