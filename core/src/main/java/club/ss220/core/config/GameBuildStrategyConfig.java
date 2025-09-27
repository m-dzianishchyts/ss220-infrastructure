package club.ss220.core.config;

import club.ss220.core.shared.GameBuild;
import club.ss220.core.spi.BanStorage;
import club.ss220.core.spi.CharacterStorage;
import club.ss220.core.spi.GameServerPort;
import club.ss220.core.spi.NoteStorage;
import club.ss220.core.spi.PlayerStorage;
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
    public Map<GameBuild, PlayerStorage> playerStorageMap(
            @Qualifier(GameConfig.BUILD_PARADISE) PlayerStorage paradiseStorage,
            @Qualifier(GameConfig.BUILD_BANDASTRATION) PlayerStorage bandastationStorage) {
        return Map.of(
                GameBuild.PARADISE, paradiseStorage,
                GameBuild.BANDASTATION, bandastationStorage
        );
    }

    @Bean
    public Map<GameBuild, BanStorage> banStorageMap(
            @Qualifier(GameConfig.BUILD_PARADISE) BanStorage paradiseStorage,
            @Qualifier(GameConfig.BUILD_BANDASTRATION) BanStorage bandastationStorage) {
        return Map.of(
                GameBuild.PARADISE, paradiseStorage,
                GameBuild.BANDASTATION, bandastationStorage
        );
    }

    @Bean
    public Map<GameBuild, NoteStorage> noteStorageMap(
            @Qualifier(GameConfig.BUILD_PARADISE) NoteStorage paradiseStorage,
            @Qualifier(GameConfig.BUILD_BANDASTRATION) NoteStorage bandastationStorage) {
        return Map.of(
                GameBuild.PARADISE, paradiseStorage,
                GameBuild.BANDASTATION, bandastationStorage
        );
    }

    @Bean
    public Map<GameBuild, CharacterStorage> characterStorageMap(
            @Qualifier(GameConfig.BUILD_PARADISE) CharacterStorage paradiseStorage) {
        return Map.of(
                GameBuild.PARADISE, paradiseStorage
                // TODO: 01.08.2025 Provide storage here when bandastation will store game characters in a database.
        );
    }
}
