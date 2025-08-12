package club.ss220.storage.paradise.spring.jpa.ban.repository;

import club.ss220.storage.paradise.spring.jpa.ban.entity.ParadiseBanEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ParadiseBanJpaRepository extends JpaRepository<ParadiseBanEntity, Integer> {

    @Query("SELECT b FROM ParadiseBanEntity b WHERE b.ckey = :ckey AND b.banDatetime <= :banDatetime ORDER BY b.banDatetime DESC")
    List<ParadiseBanEntity> findRecentPlayerBans(String ckey, LocalDateTime banDatetime, Pageable pageable);

    @Query("SELECT b FROM ParadiseBanEntity b WHERE b.ckey = :ckey ORDER BY b.banDatetime DESC")
    List<ParadiseBanEntity> findPlayerBans(String ckey, Pageable pageable);

    Integer countBansByCkey(String ckey);
}
