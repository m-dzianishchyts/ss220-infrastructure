package club.ss220.manager.config;

import club.ss220.core.application.GetAllServersStatusUseCase;
import club.ss220.core.application.GetBansUseCase;
import club.ss220.core.application.GetMemberDataUseCase;
import club.ss220.core.application.GetNotesUseCase;
import club.ss220.core.application.GetOnlinePlayersListUseCase;
import club.ss220.core.application.GetOnlineStaffListUseCase;
import club.ss220.core.application.GetServerStatusUseCase;
import club.ss220.core.application.SearchCharactersUseCase;
import club.ss220.core.config.GameConfig;
import club.ss220.core.shared.GameBuild;
import club.ss220.core.spi.BanStorage;
import club.ss220.core.spi.CharacterStorage;
import club.ss220.core.spi.GameServerPort;
import club.ss220.core.spi.NoteStorage;
import club.ss220.core.spi.PlayerStorage;
import club.ss220.core.spi.UserStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class UseCaseConfig {

    private static <T> Map<GameBuild, T> filterDisabledBuilds(Map<GameBuild, T> beanMap, GameConfig gameConfig) {
        return beanMap.entrySet().stream()
                .filter(e -> gameConfig.getBuilds().get(e.getKey()).isEnabled())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Bean
    public GetServerStatusUseCase serverStatusUseCase(Map<GameBuild, GameServerPort> gameApiClients,
                                                      GameConfig gameConfig) {
        return new GetServerStatusUseCase(filterDisabledBuilds(gameApiClients, gameConfig));
    }

    @Bean
    public GetAllServersStatusUseCase getAllServersStatusUseCase(Map<GameBuild, GameServerPort> gameApiClients,
                                                                 GameConfig gameConfig) {
        return new GetAllServersStatusUseCase(filterDisabledBuilds(gameApiClients, gameConfig));
    }

    @Bean
    public GetOnlinePlayersListUseCase playersListUseCase(Map<GameBuild, GameServerPort> gameApiClients,
                                                          GameConfig gameConfig) {
        return new GetOnlinePlayersListUseCase(filterDisabledBuilds(gameApiClients, gameConfig));
    }

    @Bean
    public GetOnlineStaffListUseCase getOnlineStaffListUseCase(Map<GameBuild, GameServerPort> gameApiClients,
                                                               GameConfig gameConfig) {
        return new GetOnlineStaffListUseCase(filterDisabledBuilds(gameApiClients, gameConfig));
    }

    @Bean
    public SearchCharactersUseCase searchCharactersUseCase(Map<GameBuild, CharacterStorage> characterStorageMap,
                                                           GameConfig gameConfig) {
        return new SearchCharactersUseCase(filterDisabledBuilds(characterStorageMap, gameConfig));
    }

    @Bean
    public GetMemberDataUseCase resolveMemberUseCase(UserStorage userStorage,
                                                     Map<GameBuild, PlayerStorage> playerStorageMap,
                                                     GameConfig gameConfig) {
        return new GetMemberDataUseCase(userStorage, filterDisabledBuilds(playerStorageMap, gameConfig));
    }

    @Bean
    public GetBansUseCase getPlayerBansUseCase(Map<GameBuild, BanStorage> banStorageMap,
                                               GameConfig gameConfig) {
        return new GetBansUseCase(filterDisabledBuilds(banStorageMap, gameConfig));
    }

    @Bean
    public GetNotesUseCase getPlayerNotesUseCase(Map<GameBuild, NoteStorage> noteStorageMap,
                                                 GameConfig gameConfig) {
        return new GetNotesUseCase(filterDisabledBuilds(noteStorageMap, gameConfig));
    }
}
