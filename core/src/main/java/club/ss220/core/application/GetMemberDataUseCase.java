package club.ss220.core.application;

import club.ss220.core.spi.PlayerStorage;
import club.ss220.core.spi.UserStorage;
import club.ss220.core.shared.*;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class GetMemberDataUseCase {

    private final UserStorage userStorage;
    private final List<PlayerStorage> playerStorages;

    public Optional<MemberData> execute(long discordId) {
        return userStorage.getUserByDiscordId(discordId).map(this::enrichWithPlayerData);
    }

    public Optional<MemberData> execute(String ckey) {
        return userStorage.getUserByCkey(ckey).map(this::enrichWithPlayerData);
    }

    private MemberData enrichWithPlayerData(UserData user) {
        String ckey = user.ckey();
        NavigableMap<GameBuild, PlayerData> gameInfo = new TreeMap<>();

        for (PlayerStorage playerStorage : playerStorages) {
            playerStorage.findByCkey(ckey).ifPresent(player -> gameInfo.put(player.gameBuild(), player));
        }
        return new MemberData(user, gameInfo);
    }
}
