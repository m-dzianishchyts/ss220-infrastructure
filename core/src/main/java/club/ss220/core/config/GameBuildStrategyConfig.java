package club.ss220.core.config;

import club.ss220.core.shared.GameBuild;
import club.ss220.core.spi.BanStorage;
import club.ss220.core.spi.CharacterStorage;
import club.ss220.core.spi.GameServerPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class GameBuildStrategyConfig {

    @Bean
    public Map<GameBuild, GameServerPort> gameApiClients(
            @Qualifier(GameConfig.BUILD_PARADISE) GameServerPort paradiseClient,
            @Qualifier(GameConfig.BUILD_BANDASTRATION) GameServerPort bandastationClient) {
        return Map.of(
                GameBuild.PARADISE, paradiseClient,
                GameBuild.BANDASTATION, bandastationClient
        );
    }

    @Bean
    public Map<GameBuild, BanStorage> banRepositoryMap(
            @Qualifier(GameConfig.BUILD_PARADISE) BanStorage paradiseRepository,
            @Qualifier(GameConfig.BUILD_BANDASTRATION) BanStorage bandastationRepository) {
        return Map.of(
                GameBuild.PARADISE, paradiseRepository,
                GameBuild.BANDASTATION, bandastationRepository
        );
    }

    @Bean
    public Map<GameBuild, CharacterStorage> characterRepositoryMap(
            @Qualifier(GameConfig.BUILD_PARADISE) CharacterStorage paradiseRepository) {
        return Map.of(
                GameBuild.PARADISE, paradiseRepository
                // TODO: 01.08.2025 Provide repository here when bandastation will store game characters in a database.
        );
    }
}
