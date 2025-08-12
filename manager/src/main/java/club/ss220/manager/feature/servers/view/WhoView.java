package club.ss220.manager.feature.servers.view;

import club.ss220.core.shared.GameServerData;
import club.ss220.manager.util.Senders;
import club.ss220.manager.view.UiConstants;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class WhoView {

    private final Senders senders;

    public void renderPlayersOnline(InteractionHook hook, GameServerData gameServer, List<String> playersOnline) {
        MessageEmbed embed = createPlayersOnlineForServerEmbed(gameServer, playersOnline);
        senders.sendEmbedEphemeral(hook, embed);
    }

    private MessageEmbed createPlayersOnlineForServerEmbed(GameServerData gameServer, List<String> playersOnline) {
        String description = playersOnline.stream()
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
