package club.ss220.manager.feature.whitelist.view;

import club.ss220.core.shared.WhitelistData;
import club.ss220.manager.shared.pagination.PageRenderer;
import club.ss220.manager.shared.presentation.Embeds;
import club.ss220.manager.shared.presentation.Formatters;
import club.ss220.manager.shared.presentation.UiConstants;
import dev.freya02.jda.emojis.unicode.Emojis;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;

@Component
public class WhitelistVerboseView extends WhitelistView implements PageRenderer<WhitelistData> {

    public WhitelistVerboseView(Embeds embeds, Formatters formatters) {
        super(embeds, formatters);
    }

    @Override
    protected String whitelistItemBlock(WhitelistData item) {
        boolean verbose = true;
        return whitelistItemBlock(item, verbose);
    }

    @Override
    public boolean enableItemSelector() {
        return true;
    }

    @Override
    public String itemOptionLabel(WhitelistData wl) {
        return "#" + wl.id();
    }

    @Override
    public MessageEmbed renderDetails(WhitelistData wl) {
        EmbedBuilder embed = toMessageEmbed(wl);
        embed.setTitle("%s **Детали WL #%d**".formatted(Emojis.PASSPORT_CONTROL.getFormatted(), wl.id()));
        embed.setColor(UiConstants.COLOR_INFO);
        return embed.build();
    }
}
