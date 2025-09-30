package club.ss220.manager.config;

import club.ss220.core.shared.GameBuild;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@Data
@Configuration
@ConfigurationProperties("ss220.discord.news")
public class IngameNewsDiscordConfig {

    @Valid
    private final Map<GameBuild, Long> channels = new EnumMap<>(GameBuild.class);

    public Optional<Long> getChannelIdByBuild(GameBuild build) {
        return Optional.ofNullable(channels.get(build));
    }
}
