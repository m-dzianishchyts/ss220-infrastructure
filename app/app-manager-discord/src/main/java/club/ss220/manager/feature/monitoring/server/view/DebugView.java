package club.ss220.manager.feature.monitoring.server.view;

import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.GameServerStatusData;
import club.ss220.manager.shared.presentation.BasicView;
import club.ss220.manager.shared.presentation.Embeds;
import club.ss220.manager.shared.presentation.Formatters;
import club.ss220.manager.shared.presentation.UiConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;

@Component
public class DebugView extends BasicView {

    private final ObjectMapper objectMapper;

    public DebugView(Embeds embeds, Formatters formatters) {
        super(embeds, formatters);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT, SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
    }

    public MessageEmbed renderServerStatus(GameServerData server, GameServerStatusData serverStatus) {
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
