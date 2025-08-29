package club.ss220.storage.paradise.spring.jpa.ban.repository;

import club.ss220.storage.paradise.spring.jpa.ban.entity.ParadiseBanEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParadiseBanJpaRepository extends JpaRepository<ParadiseBanEntity, Integer> {

    @Query("""
            SELECT b FROM ParadiseBanEntity b
            WHERE (:ckey IS NULL OR b.ckey = :ckey)
              AND (:adminCkey IS NULL OR b.adminCkey = :adminCkey)
              AND (:serverAddress IS NULL OR b.serverAddress = :serverAddress)
              AND (:roundId IS NULL OR b.roundId = :roundId)
              AND (
                    :unbanned IS NULL OR
                    (:unbanned = TRUE AND b.unbanDateTime IS NOT NULL) OR
                    (:unbanned = FALSE AND b.unbanDateTime IS NULL)
                  )
              AND (
                    :expired IS NULL OR
                    (:expired = TRUE AND b.duration >= 0 AND function('timestampadd', MINUTE, b.duration, b.banDateTime) <= CURRENT_TIMESTAMP) OR
                    (:expired = FALSE AND (b.duration < 0 OR function('timestampadd', MINUTE, b.duration, b.banDateTime) > CURRENT_TIMESTAMP))
                  )
              AND (
                    :permanent IS NULL OR
                    (:permanent = TRUE AND b.duration < 0) OR
                    (:permanent = FALSE AND b.duration >= 0)
                  )
              AND (
                    :banType IS NULL OR (
                        (:banType = 'ADMIN' AND lower(b.banType) LIKE '%admin%')
                        OR (:banType = 'ROLE' AND b.role IS NOT NULL)
                        OR (:banType = 'NORMAL' AND b.role IS NULL AND lower(b.banType) NOT LIKE '%admin%')
                    )
                  )
              ORDER BY b.banDateTime DESC
            """)
    Page<ParadiseBanEntity> findByQuery(String ckey, String adminCkey, String serverAddress, Integer roundId,
                                        Boolean unbanned, Boolean expired, Boolean permanent, String banType,
                                        Pageable pageable);
}
