package club.ss220.manager.config;

import club.ss220.manager.shared.GameServerType;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Slf4j
@Data
@Configuration
@ConfigurationProperties("ss220.discord.game")
public class GameDiscordConfig {

    @Valid
    private List<GameServerType> serverTypes;

    public Optional<GameServerType> getGameServerTypeByName(String name) {
        return serverTypes.stream().filter(it -> it.name().equalsIgnoreCase(name)).findAny();
    }
}
