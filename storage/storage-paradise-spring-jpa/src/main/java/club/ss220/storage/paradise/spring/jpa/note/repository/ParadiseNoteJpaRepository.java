package club.ss220.storage.paradise.spring.jpa.note.repository;

import club.ss220.storage.paradise.spring.jpa.note.entity.ParadiseNoteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParadiseNoteJpaRepository extends JpaRepository<ParadiseNoteEntity, Integer> {

    @Query("""
            SELECT n FROM ParadiseNoteEntity n
            WHERE n.automated = FALSE
              AND (:ckey IS NULL OR n.ckey = :ckey)
              AND (:adminCkey IS NULL OR n.adminCkey = :adminCkey)
              AND (:serverId IS NULL OR n.serverId = :serverId)
              AND (:roundId IS NULL OR n.roundId = :roundId)
              AND (:deleted IS NULL OR n.deleted = :deleted)
            ORDER BY n.timestamp DESC
            """)
    Page<ParadiseNoteEntity> findByQuery(String ckey, String adminCkey, String serverId, Integer roundId,
                                         Boolean deleted, Pageable pageable);
}
