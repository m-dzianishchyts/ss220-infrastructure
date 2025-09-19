package club.ss220.manager.feature.note.view;

import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.NoteData;
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
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotesView implements PageRenderer<NoteData> {

    private static final int SHORT_TEXT_LENGTH = 45;
    private static final int MAX_EDIT_HISTORY_LENGTH = 1000;

    private final Embeds embeds;
    private final Senders senders;
    private final Formatters formatters;

    public void renderMemberNotFound(net.dv8tion.jda.api.interactions.InteractionHook hook, MemberTarget target) {
        senders.sendEmbed(hook, embeds.error("Пользователь " + target.getDisplayString() + " не найден."));
    }

    public void renderNoNotesFound(net.dv8tion.jda.api.interactions.InteractionHook hook) {
        senders.sendEmbed(hook, embeds.info("Список заметок", "Нет заметок, удовлетворяющих условиям."));
    }

    @Override
    public MessageEmbed renderPage(PaginatedContext<NoteData> ctx) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Список заметок");
        eb.setColor(UiConstants.COLOR_INFO);

        String description = ctx.pageItems().stream()
                .map(this::noteBlock)
                .collect(Collectors.joining("\n\n"))
                .trim();

        eb.setDescription(description);
        eb.setFooter(paginationFooter(ctx));
        return eb.build();
    }

    @Override
    public boolean enableItemSelector() { return true; }

    @Override
    public String itemOptionLabel(NoteData note) {
        return "#" + note.id() + " · " + StringUtils.truncate(note.text().replace('\n', ' '), SHORT_TEXT_LENGTH);
    }

    @Override
    public MessageEmbed renderDetails(NoteData note) {
        String serverName = Optional.ofNullable(note.server()).map(GameServerData::fullName).orElse("N/A");
        String roundId = Optional.ofNullable(note.roundId()).map(String::valueOf).orElse("N/A");
        String status = note.active() ? "Активна" : "Неактивна";
        String timestamp = formatters.formatDateTime(note.timestamp());
        String deactivateCkey = Optional.ofNullable(note.deactivateCkey()).orElse("N/A");
        String editHistory = Optional.ofNullable(shortEditHistory(note)).map(s -> "```" + s + "```").orElse(null);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(Emojis.MEMO.getFormatted() + " Детали заметки #" + note.id());
        embed.setDescription(note.text());

        embed.addField("Сервер", serverName, true);
        embed.addField("Раунд", roundId, true);
        embed.addField("Статус", status, true);
        embed.addField("Игрок", note.ckey(), true);
        embed.addField("Администратор", note.adminCkey(), true);
        embed.addField("Время", timestamp, true);
        embed.addField("Деактивирована", deactivateCkey, true);

        if (editHistory != null) {
            embed.addField("История изменений", editHistory, false);
        }

        embed.setColor(UiConstants.COLOR_INFO);
        return embed.build();
    }

    private String noteBlock(NoteData note) {
        String statusText = note.active() ? "Активна" : "Неактивна";
        UnicodeEmoji statusEmoji = note.active() ? Emojis.RED_CIRCLE : Emojis.GREEN_CIRCLE;
        String dateTime = formatters.formatDateTime(note.timestamp());
        return """
                %s **Заметка #%d** — %s
                %s %s
                %s %s
                %s %s
                %s %s
                """.formatted(
                statusEmoji.getFormatted(), note.id(), statusText,
                Emojis.BUST_IN_SILHOUETTE.getFormatted(), note.ckey(),
                Emojis.COP.getFormatted(), note.adminCkey(),
                Emojis.CALENDAR.getFormatted(), dateTime,
                Emojis.MEMO.getFormatted(), shortText(note.text())
        ).trim();
    }

    private String shortText(String text) {
        return StringUtils.truncate(text.replace('\n', ' '), SHORT_TEXT_LENGTH);
    }

    private String shortEditHistory(NoteData note) {
        if (note.editHistory() == null) {
            return null;
        }
        return StringUtils.truncate(note.editHistory(), MAX_EDIT_HISTORY_LENGTH, "...");
    }
}
