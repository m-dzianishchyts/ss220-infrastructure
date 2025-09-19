package club.ss220.manager.feature.monitoring.server.view;

import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.GameServerStatusData;
import club.ss220.manager.presentation.Senders;
import club.ss220.manager.presentation.UiConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.stereotype.Component;

@Component
public class DebugView {

    private final Senders senders;
    private final ObjectMapper objectMapper;

    public DebugView(Senders senders) {
        this.senders = senders;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT, SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
    }

    public void renderServerStatus(InteractionHook hook, GameServerData server, GameServerStatusData serverStatus) {
        MessageEmbed embed = createServerStatusEmbed(server, serverStatus);
        senders.sendEmbed(hook, embed);
    }

    private MessageEmbed createServerStatusEmbed(GameServerData server, GameServerStatusData serverStatus) {
        return new EmbedBuilder()
                .setTitle("Статус сервера " + server.fullName())
                .setDescription(createServerStatusBlock(serverStatus))
                .setColor(UiConstants.COLOR_INFO)
                .build();
    }

    @SneakyThrows
    private String createServerStatusBlock(GameServerStatusData serverStatus) {
        return "```json\n" + objectMapper.writeValueAsString(serverStatus.rawData()) + "\n```";
    }
}
