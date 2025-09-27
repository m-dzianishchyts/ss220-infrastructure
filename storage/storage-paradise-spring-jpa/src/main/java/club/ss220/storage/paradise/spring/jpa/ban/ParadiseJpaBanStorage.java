package club.ss220.storage.paradise.spring.jpa.ban;

import club.ss220.core.shared.BanData;
import club.ss220.core.shared.GameServerData;
import club.ss220.core.spi.BanQuery;
import club.ss220.core.spi.BanStorage;
import club.ss220.storage.paradise.spring.jpa.ban.mapper.ParadiseBanMapper;
import club.ss220.storage.paradise.spring.jpa.ban.repository.ParadiseBanJpaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static club.ss220.core.config.GameConfig.BUILD_PARADISE;

@Component
@Qualifier(BUILD_PARADISE)
@ConditionalOnProperty(name = "spring.datasource.paradise.url")
public class ParadiseJpaBanStorage implements BanStorage {

    private final ParadiseBanJpaRepository jpaRepository;
    private final ParadiseBanMapper banMapper;

    public ParadiseJpaBanStorage(ParadiseBanJpaRepository jpaRepository, ParadiseBanMapper banMapper) {
        this.jpaRepository = jpaRepository;
        this.banMapper = banMapper;
    }

    @Override
    public Optional<BanData> findById(Integer id) {
        return jpaRepository.findById(id).map(banMapper::toBanData);
    }

    @Override
    public List<BanData> findByQuery(BanQuery query) {
        GameServerData server = query.getServer();
        String serverAddress = server != null ? server.ip() + ":" + server.port() : null;
        String banType = query.getBanType() != null ? query.getBanType().name() : null;
        PageRequest pageable = PageRequest.of(query.getPage(), query.getPageSize());
        return jpaRepository.findByQuery(query.getCkey(), query.getAdminCkey(),
                                         serverAddress, query.getRoundId(),
                                         query.getUnbanned(), query.getExpired(),
                                         query.getPermanent(), banType, pageable)
                .stream()
                .map(banMapper::toBanData)
                .toList();
    }
}
