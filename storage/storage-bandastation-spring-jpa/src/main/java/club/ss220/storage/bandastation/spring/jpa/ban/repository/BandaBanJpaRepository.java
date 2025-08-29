package club.ss220.storage.bandastation.spring.jpa.ban.repository;

import club.ss220.core.shared.BanData;
import club.ss220.storage.bandastation.spring.jpa.ban.entity.BandaStationBanEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BandaBanJpaRepository extends JpaRepository<BandaStationBanEntity, Integer> {

    @Query("""
            SELECT b FROM BandaStationBanEntity b
            WHERE (:ckey IS NULL OR b.ckey = :ckey)
              AND (:adminCkey IS NULL OR b.adminCkey = :adminCkey)
              AND (:serverIp IS NULL OR b.serverIp = :serverIp)
              AND (:serverPort IS NULL OR b.serverPort = :serverPort)
              AND (:roundId IS NULL OR b.roundId = :roundId)
              AND (
                    :unbanned IS NULL OR
                    (:unbanned = TRUE AND b.unbanDateTime IS NOT NULL) OR
                    (:unbanned = FALSE AND b.unbanDateTime IS NULL)
                  )
              AND (
                    :expired IS NULL OR
                    (:expired = TRUE AND b.expirationDateTime IS NOT NULL AND b.expirationDateTime <= CURRENT_TIMESTAMP) OR
                    (:expired = FALSE AND (b.expirationDateTime IS NULL OR b.expirationDateTime > CURRENT_TIMESTAMP))
                  )
              AND (
                    :permanent IS NULL OR
                    (:permanent = TRUE AND b.expirationDateTime IS NULL) OR
                    (:permanent = FALSE AND b.expirationDateTime IS NOT NULL)
                  )
              AND (:banType IS NULL OR b.banType = :banType)
              ORDER BY b.banDateTime DESC
            """)
    Page<BandaStationBanEntity> findByQuery(String ckey, String adminCkey,
                                            Long serverIp, Integer serverPort, Integer roundId,
                                            Boolean unbanned, Boolean expired, Boolean permanent, BanData.BanType banType,
                                            Pageable pageable);
}
