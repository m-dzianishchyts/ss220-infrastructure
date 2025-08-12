package club.ss220.core.spi;

import club.ss220.core.shared.UserData;

import java.util.Optional;

public interface UserStorage {

    Optional<UserData> getUserByCkey(String ckey);

    Optional<UserData> getUserByDiscordId(long discordId);
}
