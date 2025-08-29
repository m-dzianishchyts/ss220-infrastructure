package club.ss220.core.application;

import club.ss220.core.shared.BanData;
import club.ss220.core.shared.GameBuild;
import club.ss220.core.spi.BanQuery;
import club.ss220.core.spi.BanStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class GetBansUseCase {

    private final Map<GameBuild, BanStorage> banStorages;

    public List<BanData> execute(BanQuery query) {
        if (query.getServer() != null) {
            return banStorages.get(query.getServer().build()).findByQuery(query);
        }
        return banStorages.values().stream()
                .flatMap(storage -> storage.findByQuery(query).stream())
                .sorted(Comparator.comparing(BanData::banDateTime).reversed())
                .limit(query.getPageSize())
                .toList();
    }
}
