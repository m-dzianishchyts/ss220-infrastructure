package club.ss220.manager.feature.whitelist.view;

import club.ss220.core.shared.UserData;
import club.ss220.core.shared.WhitelistData;
import club.ss220.manager.presentation.Embeds;
import club.ss220.manager.presentation.Formatters;
import club.ss220.manager.presentation.Senders;
import club.ss220.manager.presentation.UiConstants;
import club.ss220.manager.shared.MemberTarget;
import club.ss220.manager.shared.pagination.PageRenderer;
import club.ss220.manager.shared.pagination.PaginatedContext;
import dev.freya02.jda.emojis.unicode.Emojis;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.UnicodeEmoji;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WhitelistView implements PageRenderer<WhitelistData> {

    protected final Embeds embeds;
    protected final Senders senders;
    protected final Formatters formatters;

    public MessageEmbed renderMemberNotFound(MemberTarget target) {
        return embeds.error("Пользователь " + target.getDisplayString() + " не найден.");
    }

    public MessageEmbed renderEmpty() {
        return embeds.info("Белый список", "Нет записей, удовлетворяющих условиям.");
    }

    public MessageEmbed renderCreated(WhitelistData wl) {
        EmbedBuilder embed = toMessageEmbed(wl);
        embed.setTitle(Emojis.PASSPORT_CONTROL.getFormatted() + " Новая запись в белом списке");
        embed.setColor(UiConstants.COLOR_SUCCESS);
        return embed.build();
    }

    public MessageEmbed renderUserBlacklisted(MemberTarget target) {
        String message = "Пользователь " + target.getDisplayString() + " находится в черном списке.";
        return embeds.error(message);
    }

    @Override
    public MessageEmbed renderPage(PaginatedContext<WhitelistData> ctx) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Белый список");
        eb.setColor(UiConstants.COLOR_INFO);

        String description = ctx.pageItems().stream()
                .map(this::whitelistItemBlock)
                .collect(Collectors.joining("\n\n"))
                .trim();

        eb.setDescription(description);
        eb.setFooter(paginationFooter(ctx));
        return eb.build();
    }

    protected String whitelistItemBlock(WhitelistData item) {
        boolean verbose = false;
        return whitelistItemBlock(item, verbose);
    }

    protected String whitelistItemBlock(WhitelistData item, boolean verbose) {
        String statusText = item.active() ? "Активна" : "Неактивна";
        UnicodeEmoji statusEmoji = item.active() ? Emojis.GREEN_CIRCLE : Emojis.RED_CIRCLE;
        String issued = formatters.formatDateTime(item.issueDateTime());
        String expires = formatters.formatDateTime(item.expirationDateTime());

        StringBuilder sb = new StringBuilder();
        List.of(statusEmoji.getFormatted(), " **WL #", item.id(), "** — ", statusText, '\n').forEach(sb::append);
        List.of(Emojis.VIDEO_GAME.getFormatted(), ' ', item.serverType(), '\n').forEach(sb::append);
        if (verbose) {
            String playerCkey = Optional.ofNullable(item.playerData()).map(UserData::ckey).orElseThrow();
            String adminCkey = Optional.ofNullable(item.adminData()).map(UserData::ckey).orElseThrow();
            List.of(Emojis.BUST_IN_SILHOUETTE.getFormatted(), ' ', playerCkey, '\n').forEach(sb::append);
            List.of(Emojis.COP.getFormatted(), ' ', adminCkey, '\n').forEach(sb::append);
        }
        List.of(Emojis.CALENDAR.getFormatted(), ' ', issued, " — ", expires).forEach(sb::append);
        return sb.toString().trim();
    }

    protected EmbedBuilder toMessageEmbed(WhitelistData wl) {
        String issueDateTime = formatters.formatDateTime(wl.issueDateTime());
        String expirationDateTime = formatters.formatDateTime(wl.expirationDateTime());
        String status = wl.active() ? "Активна" : "Снята";
        String playerCkey = Optional.ofNullable(wl.playerData()).map(UserData::ckey).orElseThrow();
        String adminCkey = Optional.ofNullable(wl.adminData()).map(UserData::ckey).orElseThrow();

        EmbedBuilder embed = new EmbedBuilder();
        embed.addField("Игрок", playerCkey, true);
        embed.addField("Администратор", adminCkey, true);
        embed.addField("Тип", wl.serverType(), true);
        embed.addField("Время", issueDateTime, true);
        embed.addField("Истекает", expirationDateTime, true);
        embed.addField("Статус", status, true);
        return embed;
    }
}
