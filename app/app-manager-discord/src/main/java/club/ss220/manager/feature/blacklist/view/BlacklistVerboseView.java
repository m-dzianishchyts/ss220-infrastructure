package club.ss220.manager.feature.blacklist.view;

import club.ss220.core.shared.BlacklistEntryData;
import club.ss220.manager.shared.pagination.PageRenderer;
import club.ss220.manager.shared.presentation.Embeds;
import club.ss220.manager.shared.presentation.Formatters;
import club.ss220.manager.shared.presentation.UiConstants;
import dev.freya02.jda.emojis.unicode.Emojis;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;

@Component
public class BlacklistVerboseView extends BlacklistView implements PageRenderer<BlacklistEntryData> {

    public BlacklistVerboseView(Embeds embeds, Formatters formatters) {
        super(embeds, formatters);
    }

    @Override
    protected String blacklistItemBlock(BlacklistEntryData item) {
        boolean verbose = true;
        return blacklistItemBlock(item, verbose);
    }

    @Override
    public boolean enableItemSelector() {
        return true;
    }

    @Override
    public String itemOptionLabel(BlacklistEntryData bl) {
        return "#" + bl.id() + " - " + shortReason(bl);
    }

    @Override
    public MessageEmbed renderDetails(BlacklistEntryData bl) {
        EmbedBuilder embed = toMessageEmbed(bl);
        embed.setTitle("%s **Детали BL #%d**".formatted(Emojis.NO_PEDESTRIANS.getFormatted(), bl.id()));
        embed.setColor(UiConstants.COLOR_INFO);
        return embed.build();
    }
}
