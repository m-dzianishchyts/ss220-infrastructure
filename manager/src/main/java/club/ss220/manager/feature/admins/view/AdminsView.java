package club.ss220.manager.feature.admins.view;

import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.OnlineAdminStatusData;
import club.ss220.manager.util.Senders;
import club.ss220.manager.view.GameBuildStyle;
import club.ss220.manager.view.UiConstants;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AdminsView {

    private final Senders senders;

    public void renderOnlineAdmins(InteractionHook hook,
                                   Map<GameServerData, List<OnlineAdminStatusData>> onlineAdmins) {
        MessageEmbed embed = createOnlineAdminsEmbed(onlineAdmins);
        senders.sendEmbedEphemeral(hook, embed);
    }

    private MessageEmbed createOnlineAdminsEmbed(Map<GameServerData, List<OnlineAdminStatusData>> serversAdmins) {
        List<MessageEmbed.Field> fields = groupByBuildStyle(serversAdmins).entrySet().stream()
                .map(e -> buildAdminsField(e.getKey(), e.getValue()))
                .toList();

        long totalAdmins = serversAdmins.values().stream()
                .flatMap(Collection::stream)
                .map(OnlineAdminStatusData::ckey)
                .distinct()
                .count();

        EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Админы онлайн: " + totalAdmins);
        embedBuilder.getFields().addAll(fields);
        embedBuilder.setColor(UiConstants.COLOR_INFO);
        return embedBuilder.build();
    }

    private <V> Map<GameBuildStyle, Map<GameServerData, V>> groupByBuildStyle(Map<GameServerData, V> map) {
        Function<GameServerData, GameBuildStyle> serverToStyle = e -> GameBuildStyle.fromName(e.getBuild().getName());
        return map.entrySet().stream()
                .collect(Collectors.groupingBy(
                        e -> serverToStyle.apply(e.getKey()),
                        TreeMap::new,
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
                ));
    }

    private MessageEmbed.Field buildAdminsField(GameBuildStyle buildStyle,
                                                Map<GameServerData, List<OnlineAdminStatusData>> serversAdmins) {
        String title = buildStyle.getEmoji().getFormatted() + " " + buildStyle.getName();
        String value = serversAdmins.entrySet().stream()
                .map(e -> serverAdminsBlock(e.getKey(), e.getValue()))
                .collect(Collectors.joining("\n"));
        return new MessageEmbed.Field(title, value, false);
    }

    private String serverAdminsBlock(GameServerData server, List<OnlineAdminStatusData> admins) {
        StringBuilder builder = new StringBuilder();
        builder.append("**").append(server.getName()).append("**\n");
        if (admins.isEmpty()) {
            builder.append(UiConstants.SPACE_FILLER + "Нет админов онлайн.");
            return builder.toString();
        }
        admins.forEach(a -> {
            String ranks = String.join(", ", a.ranks());
            builder.append(UiConstants.SPACE_FILLER).append(a.key()).append(" - ").append(ranks).append("\n");
        });
        return builder.toString().trim();
    }
}
