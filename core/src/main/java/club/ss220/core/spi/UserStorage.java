package club.ss220.core.spi;

import club.ss220.core.shared.UserData;

import java.util.Optional;

public interface UserStorage {

    Optional<UserData> findUserByCkey(String ckey);

    Optional<UserData> findUserByDiscordId(long discordId);

    Optional<UserData> findUserById(int id);
}
