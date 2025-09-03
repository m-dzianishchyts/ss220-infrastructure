package club.ss220.storage.bandastation.spring.jpa.note.repository;

import club.ss220.storage.bandastation.spring.jpa.note.entity.BandaStationMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BandaMessageJpaRepository extends JpaRepository<BandaStationMessageEntity, Integer> {

    @Query("""
            SELECT m FROM BandaStationMessageEntity m
            WHERE m.type = :type
              AND (:ckey IS NULL OR m.ckey = :ckey)
              AND (:adminCkey IS NULL OR m.adminCkey = :adminCkey)
              AND (:serverIp IS NULL OR m.serverIp = :serverIp)
              AND (:serverPort IS NULL OR m.serverPort = :serverPort)
              AND (:roundId IS NULL OR m.roundId = :roundId)
              AND (:deleted IS NULL OR m.deleted = :deleted)
            ORDER BY m.timestamp DESC
            """)
    Page<BandaStationMessageEntity> findByQuery(Long serverIp, Integer serverPort, Integer roundId, String type,
                                                String ckey, String adminCkey, Boolean deleted, Pageable pageable);

    Optional<BandaStationMessageEntity> findByIdAndType(Integer id, String type);
}
