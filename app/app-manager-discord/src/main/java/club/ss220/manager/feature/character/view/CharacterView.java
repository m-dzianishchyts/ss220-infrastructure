package club.ss220.manager.feature.character.view;

import club.ss220.core.shared.GameBuild;
import club.ss220.core.shared.GameCharacterData;
import club.ss220.manager.shared.pagination.PageRenderer;
import club.ss220.manager.shared.pagination.PaginatedContext;
import club.ss220.manager.shared.presentation.BasicView;
import club.ss220.manager.shared.presentation.Embeds;
import club.ss220.manager.shared.presentation.Formatters;
import club.ss220.manager.shared.presentation.UiConstants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CharacterView extends BasicView implements PageRenderer<GameCharacterData> {

    public CharacterView(Embeds embeds, Formatters formatters) {
        super(embeds, formatters);
    }

    public MessageEmbed renderNoCharactersFound(String query) {
        return embeds.error("Персонажи по запросу '" + query + "' не найдены.");
    }

    public MessageEmbed renderUnsupportedBuild(GameBuild build) {
        return embeds.error(build.getName() + " пока не поддерживает поиск персонажей.");
    }

    @Override
    public MessageEmbed renderPage(PaginatedContext<GameCharacterData> ctx) {
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
