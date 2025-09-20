package club.ss220.manager.feature.character.controller;

import club.ss220.core.application.SearchCharactersUseCase;
import club.ss220.core.shared.GameBuild;
import club.ss220.core.shared.GameCharacterData;
import club.ss220.core.shared.exception.GameBuildOperationNotSupportedException;
import club.ss220.core.spi.CharacterQuery;
import club.ss220.manager.feature.character.view.CharacterView;
import club.ss220.manager.shared.pagination.GenericPaginationController;
import club.ss220.manager.shared.presentation.Senders;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
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
    private final Senders senders;

    public void searchCharacters(IReplyCallback interaction, @Nullable GameBuild build, String name) {
        interaction.deferReply().setEphemeral(true).queue();

        CharacterQuery query = CharacterQuery.builder().build(build).name(name).build();
        try {
            List<GameCharacterData> characters = searchCharacters.getCharactersByQuery(query);
            if (characters.isEmpty()) {
                senders.sendEmbed(interaction, view.renderNoCharactersFound(name));
            } else {
                paginationController.show(interaction, characters, PAGE_SIZE, view);
            }
            log.debug("Displayed {} characters for query {}", characters.size(), query);
        } catch (GameBuildOperationNotSupportedException e) {
            log.warn("Character search not supported for build {}", e.getGameBuild());
            senders.sendEmbed(interaction, view.renderUnsupportedBuild(e.getGameBuild()));
        }
    }
}
