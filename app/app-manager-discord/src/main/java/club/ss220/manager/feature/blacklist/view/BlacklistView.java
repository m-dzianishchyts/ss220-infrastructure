package club.ss220.manager.feature.blacklist.view;

import club.ss220.core.shared.BlacklistEntryData;
import club.ss220.core.shared.UserData;
import club.ss220.core.util.StringUtils;
import club.ss220.manager.shared.pagination.PageRenderer;
import club.ss220.manager.shared.pagination.PaginatedContext;
import club.ss220.manager.shared.presentation.BasicView;
import club.ss220.manager.shared.presentation.Embeds;
import club.ss220.manager.shared.presentation.Formatters;
import club.ss220.manager.shared.presentation.UiConstants;
import dev.freya02.jda.emojis.unicode.Emojis;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.UnicodeEmoji;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BlacklistView extends BasicView implements PageRenderer<BlacklistEntryData> {

    private static final int SHORT_REASON_LENGTH = 45;

    public BlacklistView(Embeds embeds, Formatters formatters) {
        super(embeds, formatters);
    }

    public MessageEmbed renderNoEntries() {
        return embeds.info("Черный список", "Нет записей, удовлетворяющих условиям.");
    }

    public MessageEmbed renderNewEntry(BlacklistEntryData bl) {
        EmbedBuilder embed = toMessageEmbed(bl);
        embed.setTitle(Emojis.NO_PEDESTRIANS.getFormatted() + " Новая запись в черном списке");
        embed.setColor(UiConstants.COLOR_SUCCESS);
        return embed.build();
    }

    @Override
    public MessageEmbed renderPage(PaginatedContext<BlacklistEntryData> ctx) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Черный список");

        String description = ctx.pageItems().stream()
                .map(this::blacklistItemBlock)
                .collect(Collectors.joining("\n\n"))
                .trim();

        eb.setDescription(description);
        eb.setFooter(paginationFooter(ctx));
        eb.setColor(UiConstants.COLOR_INFO);
        return eb.build();
    }

    protected String blacklistItemBlock(BlacklistEntryData item) {
        boolean verbose = false;
        return blacklistItemBlock(item, verbose);
    }

    protected String blacklistItemBlock(BlacklistEntryData item, boolean verbose) {
        String statusText = item.active() ? "Активна" : "Неактивна";
        UnicodeEmoji statusEmoji = item.active() ? Emojis.GREEN_CIRCLE : Emojis.RED_CIRCLE;
        String issued = formatters.formatDateTime(item.issueDateTime());
        String expires = formatters.formatDateTime(item.expirationDateTime());

        StringBuilder sb = new StringBuilder();
        List.of(statusEmoji.getFormatted(), " **BL #", item.id(), "** — ", statusText, "\n").forEach(sb::append);
        List.of(Emojis.VIDEO_GAME.getFormatted(), ' ', item.serverType(), '\n').forEach(sb::append);
        if (verbose) {
            String playerCkey = Optional.ofNullable(item.playerData()).map(UserData::ckey).orElseThrow();
            String adminCkey = Optional.ofNullable(item.adminData()).map(UserData::ckey).orElseThrow();
            List.of(Emojis.BUST_IN_SILHOUETTE.getFormatted(), ' ', playerCkey, '\n').forEach(sb::append);
            List.of(Emojis.COP.getFormatted(), ' ', adminCkey, '\n').forEach(sb::append);
        }
        List.of(Emojis.CALENDAR.getFormatted(), ' ', issued, " — ", expires, '\n').forEach(sb::append);
        List.of(Emojis.MEMO.getFormatted(), ' ', shortReason(item), '\n').forEach(sb::append);
        return sb.toString().trim();
    }

    protected EmbedBuilder toMessageEmbed(BlacklistEntryData bl) {
        String reason = Optional.ofNullable(bl.reason()).orElse("N/A");
        String issueDateTime = formatters.formatDateTime(bl.issueDateTime());
        String expirationDateTime = formatters.formatDateTime(bl.expirationDateTime());
        String status = bl.active() ? "Активна" : "Снята";
        String playerCkey = Optional.ofNullable(bl.playerData()).map(UserData::ckey).orElseThrow();
        String adminCkey = Optional.ofNullable(bl.adminData()).map(UserData::ckey).orElseThrow();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setDescription(reason);
        embed.addField("Игрок", playerCkey, true);
        embed.addField("Администратор", adminCkey, true);
        embed.addField("Тип", bl.serverType(), true);
        embed.addField("Время", issueDateTime, true);
        embed.addField("Истекает", expirationDateTime, true);
        embed.addField("Статус", status, true);
        return embed;
    }

    protected String shortReason(BlacklistEntryData bl) {
        if (bl.reason() == null) {
            return "N/A";
        }
        return StringUtils.truncate(bl.reason().replaceAll("\\s", " "), SHORT_REASON_LENGTH);
    }
}
