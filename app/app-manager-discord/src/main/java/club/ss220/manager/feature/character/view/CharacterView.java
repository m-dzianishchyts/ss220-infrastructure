package club.ss220.manager.feature.character.view;

import club.ss220.core.shared.GameBuild;
import club.ss220.core.shared.GameCharacterData;
import club.ss220.manager.presentation.Embeds;
import club.ss220.manager.presentation.Senders;
import club.ss220.manager.presentation.UiConstants;
import club.ss220.manager.shared.pagination.PageRenderer;
import club.ss220.manager.shared.pagination.PaginatedContext;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CharacterView implements PageRenderer<GameCharacterData> {

    private final Embeds embeds;
    private final Senders senders;

    public void renderNoCharactersFound(InteractionHook hook, String query) {
        MessageEmbed embed = embeds.error("Персонажи по запросу '" + query + "' не найдены.");
        senders.sendEmbedEphemeral(hook, embed);
    }

    public void renderUnsupportedBuild(InteractionHook hook, GameBuild build) {
        MessageEmbed embed = embeds.error(build.getName() + " пока не поддерживает поиск персонажей.");
        senders.sendEmbedEphemeral(hook, embed);
    }

    @Override
    public MessageEmbed render(PaginatedContext<GameCharacterData> ctx) {
        List<GameCharacterData> pageItems = ctx.pageItems();

        List<MessageEmbed.Field> fields = pageItems.stream()
                .map(ch -> new MessageEmbed.Field(ch.realName(), ch.ckey(), true))
                .toList();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Информация о персонажах");
        embedBuilder.getFields().addAll(fields);
        embedBuilder.setFooter(paginationFooter(ctx));
        embedBuilder.setColor(UiConstants.COLOR_INFO);
        return embedBuilder.build();
    }
}
