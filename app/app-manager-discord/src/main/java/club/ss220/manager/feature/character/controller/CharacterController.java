package club.ss220.manager.feature.character.controller;

import club.ss220.core.application.SearchCharactersUseCase;
import club.ss220.core.shared.GameBuild;
import club.ss220.core.shared.GameCharacterData;
import club.ss220.core.spi.CharacterQuery;
import club.ss220.manager.feature.character.view.CharacterView;
import club.ss220.manager.shared.pagination.GenericPaginationController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class CharacterController {

    private static final int PAGE_SIZE = MessageEmbed.MAX_FIELD_AMOUNT;

    private final GenericPaginationController paginationController;
    private final CharacterView view;
    private final SearchCharactersUseCase searchCharacters;

    public void searchCharacters(InteractionHook hook, GameBuild build, String name) {
        CharacterQuery query = CharacterQuery.builder().build(build).name(name).build();
        try {
            List<GameCharacterData> characters = searchCharacters.getCharactersByQuery(query);
            if (characters.isEmpty()) {
                view.renderNoCharactersFound(hook, name);
            } else {
                paginationController.show(hook, characters, PAGE_SIZE, view);
            }

            log.debug("Displayed {} characters for query {}", characters.size(), query);
        } catch (UnsupportedOperationException _) {
            log.warn("Character search not supported for build {}", build.getName());
            view.renderUnsupportedBuild(hook, build);
        } catch (Exception e) {
            throw new RuntimeException("Error displaying characters for query " + query, e);
        }
    }
}
