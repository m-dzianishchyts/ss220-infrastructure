package club.ss220.manager.feature.server.view;

import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.GameServerStatusData;
import club.ss220.manager.presentation.Formatters;
import club.ss220.manager.presentation.GameBuildStyle;
import club.ss220.manager.presentation.Senders;
import club.ss220.manager.presentation.UiConstants;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OnlineView {

    private final Senders senders;
    private final Formatters formatters;

    public void renderPlayersOnline(InteractionHook hook, Map<GameServerData, GameServerStatusData> serversStatuses) {
        MessageEmbed embed = createPlayersOnlineEmbed(serversStatuses);
        senders.sendEmbed(hook, embed);
    }

    private MessageEmbed createPlayersOnlineEmbed(Map<GameServerData, GameServerStatusData> serversStatuses) {
        List<MessageEmbed.Field> fields = groupByBuildStyle(serversStatuses).entrySet().stream()
                .map(e -> buildOnlineField(e.getKey(), e.getValue()))
                .toList();

        int totalPlayers = serversStatuses.values().stream().mapToInt(GameServerStatusData::players).sum();

        EmbedBuilder embed = new EmbedBuilder().setTitle("Текущий онлайн: " + totalPlayers);
        embed.getFields().addAll(fields);
        embed.setFooter("(*) - команда проекта");
        embed.setColor(UiConstants.COLOR_INFO);
        return embed.build();
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

    private MessageEmbed.Field buildOnlineField(GameBuildStyle buildStyle,
                                                Map<GameServerData, GameServerStatusData> servers) {
        String title = buildStyle.getEmoji().getFormatted() + " **" + buildStyle.getName() + "**";
        String value = servers.entrySet().stream()
                .map(e -> serverOnlineBlock(e.getKey(), e.getValue()))
                .collect(Collectors.joining("\n"));
        return new MessageEmbed.Field(title, value, false);
    }

    private String serverOnlineBlock(GameServerData server, GameServerStatusData status) {
        return "**%s:** %d (%d) - %s".formatted(
                server.name(),
                status.players(),
                status.staff(),
                formatters.formatRoundDuration(status.roundDuration())
        );
    }
}
