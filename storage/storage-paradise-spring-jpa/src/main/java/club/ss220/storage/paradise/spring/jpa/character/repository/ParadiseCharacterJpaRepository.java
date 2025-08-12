package club.ss220.storage.paradise.spring.jpa.character.repository;

import club.ss220.storage.paradise.spring.jpa.character.entity.ParadiseCharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParadiseCharacterJpaRepository extends JpaRepository<ParadiseCharacterEntity, Integer> {

    List<ParadiseCharacterEntity> findByCkey(String ckey);

    List<ParadiseCharacterEntity> findByRealNameContainingIgnoreCase(String name);
}
