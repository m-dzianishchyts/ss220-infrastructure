package club.ss220.manager.feature.staff.view;

import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.OnlineStaffStatusData;
import club.ss220.manager.shared.presentation.BasicView;
import club.ss220.manager.shared.presentation.Embeds;
import club.ss220.manager.shared.presentation.Formatters;
import club.ss220.manager.shared.presentation.GameBuildStyle;
import club.ss220.manager.shared.presentation.UiConstants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class StaffView extends BasicView {

    public StaffView(Embeds embeds, Formatters formatters) {
        super(embeds, formatters);
    }

    public MessageEmbed renderOnlineStaff(Map<GameServerData, List<OnlineStaffStatusData>> serversStaff) {
        List<MessageEmbed.Field> fields = groupByBuildStyle(serversStaff).entrySet().stream()
                .map(e -> buildStaffField(e.getKey(), e.getValue()))
                .toList();

        long totalStaff = serversStaff.values().stream()
                .flatMap(Collection::stream)
                .map(OnlineStaffStatusData::ckey)
                .distinct()
                .count();

        EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Онлайн команды проекта: " + totalStaff);
        embedBuilder.getFields().addAll(fields);
        embedBuilder.setColor(UiConstants.COLOR_INFO);
        return embedBuilder.build();
    }

    private <V> Map<GameBuildStyle, Map<GameServerData, V>> groupByBuildStyle(Map<GameServerData, V> map) {
        Function<GameServerData, GameBuildStyle> serverToStyle = e -> GameBuildStyle.fromName(e.build().getName());
        return map.entrySet().stream()
                .collect(Collectors.groupingBy(
                        e -> serverToStyle.apply(e.getKey()),
                        TreeMap::new,
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
                ));
    }

    private MessageEmbed.Field buildStaffField(GameBuildStyle buildStyle,
                                               Map<GameServerData, List<OnlineStaffStatusData>> serversStaff) {
        String title = buildStyle.getEmoji().getFormatted() + " " + buildStyle.getName();
        String value = serversStaff.entrySet().stream()
                .map(e -> serverStaffBlock(e.getKey(), e.getValue()))
                .collect(Collectors.joining("\n"));
        return new MessageEmbed.Field(title, value, false);
    }

    private String serverStaffBlock(GameServerData server, List<OnlineStaffStatusData> staff) {
        StringBuilder builder = new StringBuilder();
        builder.append("**").append(server.name()).append("**\n");
        if (staff.isEmpty()) {
            builder.append(UiConstants.SPACE_FILLER + "Нет никого из команды проекта.");
            return builder.toString();
        }
        staff.forEach(a -> {
            String ranks = String.join(", ", a.ranks());
            String key = formatters.escape(a.key());
            builder.append(UiConstants.SPACE_FILLER).append(key).append(" - ").append(ranks).append("\n");
        });
        return builder.toString().trim();
    }
}
