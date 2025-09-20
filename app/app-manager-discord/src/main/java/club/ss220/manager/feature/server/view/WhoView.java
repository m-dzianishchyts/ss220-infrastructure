package club.ss220.manager.feature.server.view;

import club.ss220.core.shared.GameServerData;
import club.ss220.manager.shared.presentation.BasicView;
import club.ss220.manager.shared.presentation.Embeds;
import club.ss220.manager.shared.presentation.Formatters;
import club.ss220.manager.shared.presentation.UiConstants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WhoView extends BasicView {

    public WhoView(Embeds embeds, Formatters formatters) {
        super(embeds, formatters);
    }

    public MessageEmbed renderPlayersOnline(GameServerData gameServer, List<String> playersOnline) {
        String description = playersOnline.stream()
                .map(formatters::escape)
                .sorted(String::compareTo)
                .collect(Collectors.joining(", "));

        return new EmbedBuilder()
                .setTitle("Текущий онлайн: " + playersOnline.size())
                .setDescription(description)
                .setFooter(gameServer.fullName())
                .setColor(UiConstants.COLOR_INFO)
                .build();
    }
}
