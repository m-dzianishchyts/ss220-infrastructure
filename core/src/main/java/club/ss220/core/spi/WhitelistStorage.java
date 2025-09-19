package club.ss220.core.spi;

import club.ss220.core.shared.NewWhitelistEntry;
import club.ss220.core.shared.WhitelistData;

import java.util.List;
import java.util.Optional;

public interface WhitelistStorage {

    Optional<WhitelistData> findById(Integer id);

    List<WhitelistData> findByQuery(WhitelistQuery query);

    WhitelistData save(NewWhitelistEntry request);
}
