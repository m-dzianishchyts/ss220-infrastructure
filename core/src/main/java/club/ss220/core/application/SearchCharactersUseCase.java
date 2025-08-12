package club.ss220.core.application;

import club.ss220.core.shared.exception.GameBuildOperationNotSupportedException;
import club.ss220.core.spi.CharacterStorage;
import club.ss220.core.shared.GameBuild;
import club.ss220.core.shared.GameCharacterData;
import club.ss220.core.util.ByondUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SearchCharactersUseCase {

    private final Map<GameBuild, CharacterStorage> characterStorages;
    private final ByondUtils byondUtils;

    public SearchCharactersUseCase(Map<GameBuild, CharacterStorage> characterStorages, ByondUtils byondUtils) {
        this.characterStorages = characterStorages;
        this.byondUtils = byondUtils;
    }

    public List<GameCharacterData> getCharactersByName(GameBuild gameBuild, String name) {
        CharacterStorage storage = getStorage(gameBuild);
        List<GameCharacterData> characters = storage.findByName(name);
        return characters.stream().toList();
    }

    public List<GameCharacterData> getCharactersByCkey(GameBuild gameBuild, String ckey) {
        CharacterStorage storage = getStorage(gameBuild);
        String sanitized = byondUtils.sanitizeCkey(ckey);
        List<GameCharacterData> characters = storage.findByCkey(sanitized);
        return characters.stream().toList();
    }

    private CharacterStorage getStorage(GameBuild build) {
        return Optional.ofNullable(characterStorages.get(build))
                .orElseThrow(() -> new GameBuildOperationNotSupportedException(
                        build, "Character storage is not available for build: " + build.getName()));
    }
}
