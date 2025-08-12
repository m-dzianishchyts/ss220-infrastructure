package club.ss220.storage.paradise.spring.jpa.ban;

import club.ss220.storage.paradise.spring.jpa.ban.entity.ParadiseBanEntity;
import club.ss220.storage.paradise.spring.jpa.ban.repository.ParadiseBanJpaRepository;
import club.ss220.core.shared.BanData;
import club.ss220.core.spi.BanStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static club.ss220.core.config.GameConfig.BUILD_PARADISE;

@Component
@Qualifier(BUILD_PARADISE)
public class ParadiseJpaBanStorage implements BanStorage {

    private final ParadiseBanJpaRepository jpaRepository;

    public ParadiseJpaBanStorage(ParadiseBanJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<BanData> findById(Integer id) {
        return jpaRepository.findById(id).map(this::toBan);
    }

    @Override
    public List<BanData> findRecentPlayerBans(String ckey, LocalDateTime since, int page, int size) {
        var pageable = PageRequest.of(page, size);
        List<ParadiseBanEntity> bans = jpaRepository.findRecentPlayerBans(ckey, since, pageable);
        return bans.stream().map(this::toBan).toList();
    }

    @Override
    public List<BanData> findPlayerBans(String ckey, int page, int size) {
        var pageable = PageRequest.of(page, size);
        List<ParadiseBanEntity> bans = jpaRepository.findPlayerBans(ckey, pageable);
        return bans.stream().map(this::toBan).toList();
    }

    @Override
    public int countBansByCkey(String ckey) {
        return jpaRepository.countBansByCkey(ckey);
    }

    private BanData toBan(ParadiseBanEntity ban) {
        return BanData.builder()
                .id(ban.getId())
                .ckey(ban.getCkey())
                .adminCkey(ban.getAdminCkey())
                .reason(ban.getReason())
                .banTime(ban.getBanDatetime())
                .unbanTime(ban.getUnbanDatetime())
                .banType(ban.getBanType())
                .isActive(ban.isActive())
                .build();
    }
}
