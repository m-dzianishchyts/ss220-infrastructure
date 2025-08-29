package club.ss220.storage.paradise.spring.jpa.character;

import club.ss220.core.shared.GameCharacterData;
import club.ss220.core.spi.CharacterQuery;
import club.ss220.core.spi.CharacterStorage;
import club.ss220.storage.paradise.spring.jpa.character.mapper.ParadiseCharacterMapper;
import club.ss220.storage.paradise.spring.jpa.character.repository.ParadiseCharacterJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
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
    public List<GameCharacterData> findByQuery(CharacterQuery query) {
        PageRequest pageable = PageRequest.of(query.getPage(), query.getPageSize());
        return repository.findByQuery(query.getName(), pageable)
                .getContent()
                .stream()
                .map(characterMapper::toCharacterData)
                .toList();
    }
}
