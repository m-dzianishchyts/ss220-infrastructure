package club.ss220.manager.feature.characters.controller;

import club.ss220.core.application.SearchCharactersUseCase;
import club.ss220.core.shared.GameBuild;
import club.ss220.core.shared.GameCharacterData;
import club.ss220.manager.feature.characters.view.CharacterView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class CharacterController {

    private final CharacterView view;
    private final SearchCharactersUseCase searchCharacters;

    public void searchCharacters(InteractionHook hook, GameBuild build, String name) {
        try {
            List<GameCharacterData> characters = searchCharacters.getCharactersByName(build, name);
            if (characters.isEmpty()) {
                view.renderNoCharactersFound(hook, name);
                log.debug("Found 0 characters for query '{}' , build {}", name, build.getName());
                return;
            }
            view.renderCharactersInfo(hook, characters);

            log.debug("Displayed {} characters for query '{}' , build {}", characters.size(), name, build.getName());
        } catch (UnsupportedOperationException e) {
            log.warn("Character search not supported for build {}", build.getName());
            view.renderUnsupportedBuild(hook, build);
        } catch (Exception e) {
            throw new RuntimeException("Error displaying characters for query '" + name + "'", e);
        }
    }
}
