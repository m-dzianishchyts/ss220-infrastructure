package club.ss220.manager.config;

import club.ss220.core.application.GetAllServersStatusUseCase;
import club.ss220.core.application.GetBansUseCase;
import club.ss220.core.application.GetMemberDataUseCase;
import club.ss220.core.application.GetOnlinePlayersListUseCase;
import club.ss220.core.application.GetOnlineStaffListUseCase;
import club.ss220.core.application.GetServerStatusUseCase;
import club.ss220.core.application.SearchCharactersUseCase;
import club.ss220.core.shared.GameBuild;
import club.ss220.core.spi.BanStorage;
import club.ss220.core.spi.CharacterStorage;
import club.ss220.core.spi.GameServerPort;
import club.ss220.core.spi.PlayerStorage;
import club.ss220.core.spi.UserStorage;
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
    public GetOnlinePlayersListUseCase playersListUseCase(Map<GameBuild, GameServerPort> gameApiClients) {
        return new GetOnlinePlayersListUseCase(gameApiClients);
    }

    @Bean
    public GetOnlineStaffListUseCase getOnlineStaffListUseCase(Map<GameBuild, GameServerPort> gameApiClients) {
        return new GetOnlineStaffListUseCase(gameApiClients);
    }

    @Bean
    public SearchCharactersUseCase searchCharactersUseCase(Map<GameBuild, CharacterStorage> characterStorageMap) {
        return new SearchCharactersUseCase(characterStorageMap);
    }

    @Bean
    public GetMemberDataUseCase resolveMemberUseCase(UserStorage userStorage,
                                                     List<PlayerStorage> playerRepositories) {
        return new GetMemberDataUseCase(userStorage, playerRepositories);
    }

    @Bean
    public GetBansUseCase getPlayerBansUseCase(Map<GameBuild, BanStorage> banStorageMap) {
        return new GetBansUseCase(banStorageMap);
    }
}
