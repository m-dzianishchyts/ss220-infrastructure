package club.ss220.manager.shared.application;

import club.ss220.core.shared.UserData;
import club.ss220.core.spi.UserStorage;
import club.ss220.manager.shared.MemberTarget;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserDataProvider {

    private final UserStorage userStorage;

    public Optional<UserData> getByTarget(MemberTarget target) {
        if (target.discordId() != null) {
            return getByDiscordId(target.discordId());
        }
        if (target.ckey() != null) {
            return getByCkey(target.ckey());
        }
        throw new IllegalArgumentException("Member target must have a discordId or a ckey");
    }

    private Optional<UserData> getByDiscordId(Long userId) {
        Optional<UserData> user = userStorage.findUserByDiscordId(userId);
        if (user.isPresent()) {
            log.debug("Successfully resolved discord {} to user {}", userId, user.get().id());
        } else {
            log.debug("No user found for discord: {}", userId);
        }

        return user;
    }

    private Optional<UserData> getByCkey(String ckey) {
        Optional<UserData> user = userStorage.findUserByCkey(ckey);
        if (user.isPresent()) {
            log.debug("Successfully resolved ckey {} to user {}", ckey, user.get().id());
        } else {
            log.debug("No user found for ckey: {}", ckey);
        }

        return user;
    }
}
