package club.ss220.manager.feature.ban.view;

import club.ss220.core.shared.BanData;
import club.ss220.core.shared.GameServerData;
import club.ss220.core.util.StringUtils;
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
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BansView implements PageRenderer<BanData> {

    public static final int SHORT_REASON_LENGTH = 45;
    public static final int MAX_EDIT_HISTORY_LENGTH = 1000;

    private final Embeds embeds;
    private final Senders senders;
    private final Formatters formatters;

    public void renderMemberNotFound(InteractionHook hook, MemberTarget target) {
        senders.sendEmbed(hook, embeds.error("Пользователь " + target.getDisplayString() + " не найден."));
    }

    public void renderNoBansFound(InteractionHook hook) {
        senders.sendEmbed(hook, embeds.info("Список блокировок", "Нет блокировок, удовлетворяющих условиям."));
    }

    @Override
    public MessageEmbed renderPage(PaginatedContext<BanData> ctx) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Список блокировок");
        embedBuilder.setColor(UiConstants.COLOR_INFO);

        String description = ctx.pageItems().stream()
                .map(this::banBlock)
                .collect(Collectors.joining("\n\n"))
                .trim();

        embedBuilder.setDescription(description);
        embedBuilder.setFooter(paginationFooter(ctx));
        return embedBuilder.build();
    }

    @Override
    public boolean enableItemSelector() {
        return true;
    }

    @Override
    public String itemOptionLabel(BanData ban) {
        return "#" + ban.id() + " - " + shortReason(ban);
    }

    @Override
    public MessageEmbed renderDetails(BanData ban) {
        String serverName = Optional.ofNullable(ban.server()).map(GameServerData::fullName).orElse("N/A");
        String roundId = Optional.ofNullable(ban.roundId()).map(String::valueOf).orElse("N/A");
        String unbanDateTime = Optional.ofNullable(ban.unbanDateTime()).map(formatters::formatDateTime).orElse("N/A");
        String banType = ban.banType() + (ban.role() != null ? (" (" + ban.role() + ")") : "");
        String status = ban.isActive() ? "Активна" : "Снята";
        String banDateTime = formatters.formatDateTime(ban.banDateTime());
        String duration = Optional.ofNullable(ban.duration()).map(formatters::formatDuration).orElse("Бессрочно");
        String expirationDateTime = Optional.ofNullable(ban.expirationDateTime())
                .map(formatters::formatDateTime)
                .orElse("Бессрочно");
        String unbanAdminCkey = Optional.ofNullable(ban.unbanAdminCkey()).orElse("N/A");
        String editHistory = Optional.ofNullable(shortEditHistory(ban)).map(s -> "```" + s + "```").orElse(null);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("%s **Детали блокировки #%d**".formatted(Emojis.OCTAGONAL_SIGN.getFormatted(), ban.id()));
        embed.setDescription(ban.reason());

        embed.addField("Сервер", serverName, true);
        embed.addField("Раунд", roundId, true);
        embed.addField("Тип", banType, true);
        embed.addField("Нарушитель", ban.ckey(), true);
        embed.addField("Администратор", ban.adminCkey(), true);
        embed.addField("Статус", status, true);
        embed.addField("Время", banDateTime, true);
        embed.addField("Длительность", duration, true);
        embed.addField("Истекает", expirationDateTime, true);
        embed.addField("Досрочное снятие", unbanDateTime, true);
        embed.addField("Администратор", unbanAdminCkey, true);
        embed.addField("", "", true); // empty field just to align them to grid, remove if not needed

        if (editHistory != null) {
            embed.addField("История изменений", editHistory, false);
        }

        embed.setColor(UiConstants.COLOR_INFO);
        return embed.build();
    }

    private String banBlock(BanData ban) {
        String banType = ban.role() != null ? "Блокировка роли (" + ban.role() + ")" : "Блокировка";
        String statusText = ban.isActive() ? "Активна" : "Снята";
        UnicodeEmoji statusEmoji = ban.isActive() ? Emojis.RED_CIRCLE : Emojis.GREEN_CIRCLE;
        UnicodeEmoji expiredEmoji = ban.isExpired() ? Emojis.WHITE_CHECK_MARK : Emojis.OCTAGONAL_SIGN;
        String banDateTime = formatters.formatDateTime(ban.banDateTime());
        String expirationDateTime = Optional.ofNullable(ban.expirationDateTime())
                .map(formatters::formatDateTime)
                .orElse("Бессрочно");

        return """
                %s **%s #%d** — %s
                %s %s
                %s %s
                %s %s — %s %s
                %s %s
                """.formatted(
                statusEmoji.getFormatted(), banType, ban.id(), statusText,
                Emojis.BUST_IN_SILHOUETTE.getFormatted(), ban.ckey(),
                Emojis.COP.getFormatted(), ban.adminCkey(),
                Emojis.CALENDAR.getFormatted(), banDateTime, expiredEmoji.getFormatted(), expirationDateTime,
                Emojis.MEMO.getFormatted(), shortReason(ban)
        ).trim();
    }

    private String shortReason(BanData ban) {
        return StringUtils.truncate(ban.reason().replaceAll("\\s", " "), SHORT_REASON_LENGTH);
    }

    private String shortEditHistory(BanData ban) {
        if (ban.editHistory() == null) {
            return null;
        }
        return StringUtils.truncate(ban.editHistory(), MAX_EDIT_HISTORY_LENGTH, "...");
    }
}
