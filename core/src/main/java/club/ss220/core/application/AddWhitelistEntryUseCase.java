package club.ss220.core.application;

import club.ss220.core.shared.NewWhitelistEntry;
import club.ss220.core.shared.WhitelistData;
import club.ss220.core.spi.UserStorage;
import club.ss220.core.spi.WhitelistStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddWhitelistEntryUseCase {

    private final WhitelistStorage whitelistStorage;
    private final UserStorage userStorage;

    public WhitelistData execute(NewWhitelistEntry request) {
        WhitelistData created = whitelistStorage.save(request);
        WhitelistData.WhitelistDataBuilder builder = created.toBuilder();
        userStorage.findUserById(created.playerId()).ifPresent(builder::playerData);
        userStorage.findUserById(created.adminId()).ifPresent(builder::adminData);
        return builder.build();
    }
}
