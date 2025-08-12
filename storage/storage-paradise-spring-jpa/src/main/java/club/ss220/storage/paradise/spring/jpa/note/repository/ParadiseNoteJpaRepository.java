package club.ss220.storage.paradise.spring.jpa.note.repository;

import club.ss220.storage.paradise.spring.jpa.note.entity.ParadiseNoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParadiseNoteJpaRepository extends JpaRepository<ParadiseNoteEntity, Integer> {
}
