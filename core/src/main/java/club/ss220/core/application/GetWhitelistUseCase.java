package club.ss220.core.application;

import club.ss220.core.shared.WhitelistData;
import club.ss220.core.spi.UserStorage;
import club.ss220.core.spi.WhitelistQuery;
import club.ss220.core.spi.WhitelistStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetWhitelistUseCase {

    private final WhitelistStorage whitelistStorage;
    private final UserStorage userStorage;

    public List<WhitelistData> execute(WhitelistQuery query) {
        List<WhitelistData> list = whitelistStorage.findByQuery(query);
        return list.stream().map(this::enrichWithUserData).toList();
    }

    private WhitelistData enrichWithUserData(WhitelistData whitelistData) {
        WhitelistData.WhitelistDataBuilder builder = whitelistData.toBuilder();
        userStorage.findUserById(whitelistData.playerId()).ifPresent(builder::playerData);
        userStorage.findUserById(whitelistData.adminId()).ifPresent(builder::adminData);
        return builder.build();
    }
}
