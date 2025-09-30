package club.ss220.manager.feature.news.view;

import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.IngamePost;
import club.ss220.manager.shared.presentation.Formatters;
import club.ss220.manager.shared.presentation.UiConstants;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IngameNewsView {

    public static final String ATTACHMENT_NAME = "article_photo.png";

    private final Formatters formatters;

    public MessageEmbed renderPost(IngamePost ingamePost, GameServerData gameServer) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(ingamePost.title());
        embed.addField(ingamePost.channelName() + " сообщает", ingamePost.body(), false);
        embed.setFooter(buildFooter(ingamePost, gameServer));
        if (ingamePost.image() != null) {
            embed.setImage("attachment://" + ATTACHMENT_NAME);
        }
        embed.setColor(UiConstants.COLOR_INFO);
        return embed.build();
    }

    private String buildFooter(IngamePost ingamePost, GameServerData gameServer) {
        String stationTime = formatters.formatTime(ingamePost.publishTime());
        return "%s\nКод - %s, %s с начала смены.\n\n%s - %d - %s".formatted(
                ingamePost.author(),
                ingamePost.securityLevel(),
                stationTime,
                gameServer.fullName(),
                ingamePost.roundId(),
                ingamePost.authorCkey()
        );
    }
}
