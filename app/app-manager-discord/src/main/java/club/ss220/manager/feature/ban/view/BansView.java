package club.ss220.manager.feature.ban.view;

import club.ss220.core.shared.BanData;
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

    private final Embeds embeds;
    private final Senders senders;
    private final Formatters formatters;

    public void renderMemberNotFound(InteractionHook hook, MemberTarget target) {
        senders.sendEmbedEphemeral(hook, embeds.error("Пользователь " + target.getDisplayString() + " не найден."));
    }

    @Override
    public MessageEmbed render(PaginatedContext<BanData> ctx) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Список блокировок");
        embedBuilder.setColor(UiConstants.COLOR_INFO);
        if (ctx.pageItems().isEmpty()) {
            embedBuilder.setDescription("Нет блокировок, удовлетворяющих условиям.");
            return embedBuilder.build();
        }

        String description = ctx.pageItems().stream()
                .map(this::banBlock)
                .collect(Collectors.joining("\n\n"))
                .trim();

        embedBuilder.setDescription(description);
        embedBuilder.setFooter(paginationFooter(ctx));
        return embedBuilder.build();
    }

    private String banBlock(BanData ban) {
        String banType = ban.role() != null ? "Блокировка роли (" + ban.role() + ")" : "Блокировка";
        String statusText = ban.isActive() ? "Активна" : "Снята";
        UnicodeEmoji statusEmoji = ban.isActive() ? Emojis.RED_CIRCLE : Emojis.GREEN_CIRCLE;
        UnicodeEmoji expiredEmoji = ban.isExpired() ? Emojis.WHITE_CHECK_MARK : Emojis.OCTAGONAL_SIGN;
        String banDateTimeString = formatters.formatDateTime(ban.banDateTime());
        String unbanDateTimeString = Optional.ofNullable(ban.expirationDateTime())
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
                Emojis.CALENDAR.getFormatted(), banDateTimeString, expiredEmoji.getFormatted(), unbanDateTimeString,
                Emojis.MEMO.getFormatted(), ban.getShortReason()
        ).trim();
    }
}
