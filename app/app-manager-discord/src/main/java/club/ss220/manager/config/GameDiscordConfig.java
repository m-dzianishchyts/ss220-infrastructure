package club.ss220.manager.config;

import club.ss220.core.shared.GameServerType;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Data
@Configuration
@ConfigurationProperties("ss220.discord.game")
public class GameDiscordConfig {

    @Valid
    private List<GameServerTypeConfig> whitelists = new ArrayList<>();
    @Valid
    private List<GameServerType> serverTypes = new ArrayList<>();

    @PostConstruct
    public void init() {
        serverTypes.addAll(whitelists.stream().map(GameServerTypeConfig::name).map(GameServerType::fromName).toList());
    }

    public Optional<GameServerType> getServerType(String name) {
        return serverTypes.stream().filter(it -> it.name().equalsIgnoreCase(name)).findAny();
    }

    public Optional<GameServerTypeConfig> getServerTypeConfig(String name) {
        return whitelists.stream().filter(it -> it.name().equalsIgnoreCase(name)).findAny();
    }
    
    public Optional<GameServerTypeConfig> getServerTypeConfig(GameServerType serverType) {
        return getServerTypeConfig(serverType.name());
    }
}
