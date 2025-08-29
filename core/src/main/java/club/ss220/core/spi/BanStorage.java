package club.ss220.core.spi;

import club.ss220.core.shared.BanData;

import java.util.List;
import java.util.Optional;

public interface BanStorage {

    Optional<BanData> findById(Integer id);

    List<BanData> findByQuery(BanQuery query);
}
