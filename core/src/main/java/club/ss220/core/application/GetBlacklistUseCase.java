package club.ss220.core.application;

import club.ss220.core.shared.BlacklistEntryData;
import club.ss220.core.spi.BlacklistQuery;
import club.ss220.core.spi.BlacklistStorage;
import club.ss220.core.spi.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetBlacklistUseCase {

    private final BlacklistStorage blacklistStorage;
    private final UserStorage userStorage;

    public List<BlacklistEntryData> execute(BlacklistQuery query) {
        List<BlacklistEntryData> list = blacklistStorage.findByQuery(query);
        return list.stream().map(this::enrichWithUserData).toList();
    }

    private BlacklistEntryData enrichWithUserData(BlacklistEntryData blacklistEntryData) {
        BlacklistEntryData.BlacklistEntryDataBuilder builder = blacklistEntryData.toBuilder();
        userStorage.findUserById(blacklistEntryData.playerId()).ifPresent(builder::playerData);
        userStorage.findUserById(blacklistEntryData.adminId()).ifPresent(builder::adminData);
        return builder.build();
    }
}
