package club.ss220.core.spi;

import club.ss220.core.shared.BlacklistEntryData;
import club.ss220.core.shared.NewBlacklistEntry;

import java.util.List;
import java.util.Optional;

public interface BlacklistStorage {

    Optional<BlacklistEntryData> findById(Integer id);

    List<BlacklistEntryData> findByQuery(BlacklistQuery query);

    BlacklistEntryData save(NewBlacklistEntry request);
}
