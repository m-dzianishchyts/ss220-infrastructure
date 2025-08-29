package club.ss220.storage.paradise.spring.jpa.character.repository;

import club.ss220.storage.paradise.spring.jpa.character.entity.ParadiseCharacterEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParadiseCharacterJpaRepository extends JpaRepository<ParadiseCharacterEntity, Integer> {

    @Query("SELECT c FROM ParadiseCharacterEntity c WHERE c.realName LIKE %?1% ORDER BY c.ckey, c.realName")
    Page<ParadiseCharacterEntity> findByQuery(String name, Pageable pageable);
}
