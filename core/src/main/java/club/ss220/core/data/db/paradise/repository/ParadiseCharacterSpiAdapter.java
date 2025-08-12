package club.ss220.core.data.db.paradise.repository;

import club.ss220.core.config.GameConfig;
import club.ss220.core.data.db.CharacterRepositoryAdapter;
import club.ss220.core.data.mapper.Mappers;
import club.ss220.core.shared.GameCharacter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Deprecated
@Repository
@ConditionalOnBean(ParadiseCharacterRepository.class)
@Qualifier(GameConfig.BUILD_PARADISE)
public class ParadiseCharacterSpiAdapter implements CharacterRepositoryAdapter {

    private final ParadiseCharacterRepository repository;
    private final Mappers mappers;

    public ParadiseCharacterSpiAdapter(ParadiseCharacterRepository repository, Mappers mappers) {
        this.repository = repository;
        this.mappers = mappers;
    }

    @Override
    public List<GameCharacter> findByCkey(String ckey) {
        return repository.findByCkey(ckey).stream().map(mappers::toGameCharacter).toList();
    }

    @Override
    public List<GameCharacter> findByName(String name) {
        return repository.findByRealNameContainingIgnoreCase(name).stream().map(mappers::toGameCharacter).toList();
    }
}
