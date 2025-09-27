package club.ss220.storage.bandastation.spring.jpa.ban;

import club.ss220.core.shared.BanData;
import club.ss220.core.shared.GameServerData;
import club.ss220.core.spi.BanQuery;
import club.ss220.core.spi.BanStorage;
import club.ss220.core.util.InetUtils;
import club.ss220.storage.bandastation.spring.jpa.ban.mapper.BandaStationBanMapper;
import club.ss220.storage.bandastation.spring.jpa.ban.repository.BandaBanJpaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static club.ss220.core.config.GameConfig.BUILD_BANDASTRATION;

@Component
@Qualifier(BUILD_BANDASTRATION)
@ConditionalOnProperty(name = "spring.datasource.bandastation.url")
public class BandaStationJpaBanStorage implements BanStorage {

    private final BandaBanJpaRepository repository;
    private final BandaStationBanMapper mapper;

    public BandaStationJpaBanStorage(BandaBanJpaRepository repository, BandaStationBanMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<BanData> findById(Integer id) {
        return repository.findById(id).map(mapper::toBanData);
    }

    @Override
    public List<BanData> findByQuery(BanQuery query) {
        GameServerData server = query.getServer();
        Long serverIp = server != null ? InetUtils.toCompactIPv4(server.ip()) : null;
        Integer serverPort = server != null ? server.port() : null;
        PageRequest pageable = PageRequest.of(query.getPage(), query.getPageSize());
        return repository.findByQuery(query.getCkey(), query.getAdminCkey(), serverIp, serverPort, query.getRoundId(),
                                      query.getUnbanned(), query.getExpired(), query.getPermanent(), query.getBanType(),
                                      pageable)
                .stream()
                .map(mapper::toBanData)
                .toList();
    }
}
