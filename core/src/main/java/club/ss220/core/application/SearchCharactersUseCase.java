package club.ss220.core.application;

import club.ss220.core.shared.GameBuild;
import club.ss220.core.shared.GameCharacterData;
import club.ss220.core.shared.exception.GameBuildOperationNotSupportedException;
import club.ss220.core.spi.CharacterQuery;
import club.ss220.core.spi.CharacterStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class SearchCharactersUseCase {

    private final Map<GameBuild, CharacterStorage> characterStorages;

    public List<GameCharacterData> getCharactersByQuery(CharacterQuery query) {
        if (query.getBuild() == null) {
            return characterStorages.values().stream()
                    .flatMap(storage -> storage.findByQuery(query).stream())
                    .sorted(Comparator.comparing(GameCharacterData::ckey).thenComparing(GameCharacterData::realName))
                    .toList();
        }

        CharacterStorage storage = getStorage(query.getBuild());
        return storage.findByQuery(query);
    }

    private CharacterStorage getStorage(GameBuild build) {
        return Optional.ofNullable(characterStorages.get(build))
                .orElseThrow(() -> new GameBuildOperationNotSupportedException(
                        build, "Character storage is not available for build: " + build.getName()));
    }
}
