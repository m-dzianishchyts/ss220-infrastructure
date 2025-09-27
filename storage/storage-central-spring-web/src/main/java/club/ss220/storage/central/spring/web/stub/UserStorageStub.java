package club.ss220.storage.central.spring.web.stub;

import club.ss220.core.shared.UserData;
import club.ss220.core.spi.UserStorage;
import club.ss220.storage.central.spring.web.exception.StorageDisabledException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@ConditionalOnProperty(name = "ss220.api.central.endpoint", havingValue = "false", matchIfMissing = true)
public class UserStorageStub implements UserStorage {

    @Override
    public Optional<UserData> findUserByCkey(String ckey) {
        throw new StorageDisabledException(UserStorage.class);
    }

    @Override
    public Optional<UserData> findUserByDiscordId(long discordId) {
        throw new StorageDisabledException(UserStorage.class);
    }

    @Override
    public Optional<UserData> findUserById(int id) {
        throw new StorageDisabledException(UserStorage.class);
    }
}
