package club.ss220.manager.feature.news.listener;

import club.ss220.core.config.GameConfig;
import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.IngamePost;
import club.ss220.core.shared.event.NewsPublishedEvent;
import club.ss220.manager.config.IngameNewsDiscordConfig;
import club.ss220.manager.feature.news.view.IngameNewsView;
import io.github.freya022.botcommands.api.core.annotations.BEventListener;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.FileUpload;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class IngameNewsEventListener {

    private final IngameNewsDiscordConfig ingameNewsConfig;
    private final GameConfig gameConfig;
    private final IngameNewsView view;

    @Nullable
    private JDA jda;

    @BEventListener
    public void onReady(ReadyEvent event) {
        jda = event.getJDA();
    }

    @EventListener
    public void onNewsPublished(NewsPublishedEvent event) {
        if (jda == null) {
            log.error("JDA is not ready, skipping news publishing");
            return;
        }

        IngamePost post = event.post();
        String serverHost = post.serverHost();
        int serverPort = post.serverPort();
        Optional<GameServerData> gameServer = gameConfig.getServerByAddress(serverHost, serverPort);
        if (gameServer.isEmpty()) {
            log.error("Game server not found for address {}:{}, skipping news publishing", serverHost, serverPort);
            return;
        }

        Optional<Long> channelId = ingameNewsConfig.getChannelIdByBuild(gameServer.get().build());
        if (channelId.isEmpty()) {
            log.warn("News channel not configured for build {}, skipping news publishing", gameServer.get().build());
            return;
        }
        TextChannel channel = jda.getTextChannelById(channelId.get());
        if (channel == null) {
            log.error("News channel not found: {}", channelId.get());
            return;
        }

        MessageEmbed embed = view.renderPost(post, gameServer.get());
        MessageCreateAction messageCreateAction = channel.sendMessageEmbeds(embed).setAllowedMentions(List.of());
        if (post.image() == null) {
            messageCreateAction.queue();
            log.debug("Displayed news without image");
            return;
        }
        try (FileUpload file = buildImage(post.image())) {
            messageCreateAction.addFiles(file);
            messageCreateAction.queue();
            log.debug("Displayed news with image");
        } catch (IOException e) {
            log.error("Failed to upload image", e);
        }
    }

    private FileUpload buildImage(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        return FileUpload.fromData(outputStream.toByteArray(), IngameNewsView.ATTACHMENT_NAME);
    }
}
