package club.ss220.manager.config;

import club.ss220.core.application.GetAllAdminsListUseCase;
import club.ss220.core.application.GetAllServersStatusUseCase;
import club.ss220.core.application.GetPlayersListUseCase;
import club.ss220.core.application.GetServerStatusUseCase;
import club.ss220.core.application.GetMemberDataUseCase;
import club.ss220.core.application.SearchCharactersUseCase;
import club.ss220.core.shared.GameBuild;
import club.ss220.core.spi.CharacterStorage;
import club.ss220.core.spi.PlayerStorage;
import club.ss220.core.spi.GameServerPort;
import club.ss220.core.spi.UserStorage;
import club.ss220.core.util.ByondUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class UseCaseConfig {

    @Bean
    public GetServerStatusUseCase serverStatusUseCase(Map<GameBuild, GameServerPort> gameApiClients) {
        return new GetServerStatusUseCase(gameApiClients);
    }

    @Bean
    public GetAllServersStatusUseCase getAllServersStatusUseCase(Map<GameBuild, GameServerPort> gameApiClients) {
        return new GetAllServersStatusUseCase(gameApiClients);
    }

    @Bean
    public GetPlayersListUseCase playersListUseCase(Map<GameBuild, GameServerPort> gameApiClients) {
        return new GetPlayersListUseCase(gameApiClients);
    }

    @Bean
    public GetAllAdminsListUseCase getAllAdminsListUseCase(Map<GameBuild, GameServerPort> gameApiClients) {
        return new GetAllAdminsListUseCase(gameApiClients);
    }

    @Bean
    public SearchCharactersUseCase searchCharactersUseCase(Map<GameBuild, CharacterStorage> characterRepositoryMap,
                                                           ByondUtils byondUtils) {
        return new SearchCharactersUseCase(characterRepositoryMap, byondUtils);
    }

    @Bean
    public GetMemberDataUseCase resolveMemberUseCase(UserStorage userStorage,
                                                     List<PlayerStorage> playerRepositories) {
        return new GetMemberDataUseCase(userStorage, playerRepositories);
    }
}
