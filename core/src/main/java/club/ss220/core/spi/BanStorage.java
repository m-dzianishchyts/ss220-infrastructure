package club.ss220.core.spi;

import club.ss220.core.shared.BanData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BanStorage {

    Optional<BanData> findById(Integer id);

    List<BanData> findRecentPlayerBans(String ckey, LocalDateTime since, int page, int size);

    List<BanData> findPlayerBans(String ckey, int page, int size);

    int countBansByCkey(String ckey);
}
