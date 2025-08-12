package club.ss220.storage.bandastation.spring.jpa.player.repository;

import club.ss220.storage.bandastation.spring.jpa.player.entity.BandaStationPlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BandaStationPlayerJpaRepository extends JpaRepository<BandaStationPlayerEntity, String> {

    Optional<BandaStationPlayerEntity> findByCkey(String ckey);
}
