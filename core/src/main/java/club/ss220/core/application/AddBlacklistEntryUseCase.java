package club.ss220.core.application;

import club.ss220.core.shared.BlacklistEntryData;
import club.ss220.core.shared.NewBlacklistEntry;
import club.ss220.core.spi.BlacklistStorage;
import club.ss220.core.spi.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddBlacklistEntryUseCase {

    private final BlacklistStorage blacklistStorage;
    private final UserStorage userStorage;

    public BlacklistEntryData execute(NewBlacklistEntry request) {
        BlacklistEntryData created = blacklistStorage.save(request);
        BlacklistEntryData.BlacklistEntryDataBuilder builder = created.toBuilder();
        userStorage.findUserById(created.playerId()).ifPresent(builder::playerData);
        userStorage.findUserById(created.adminId()).ifPresent(builder::adminData);
        return builder.build();
    }
}
