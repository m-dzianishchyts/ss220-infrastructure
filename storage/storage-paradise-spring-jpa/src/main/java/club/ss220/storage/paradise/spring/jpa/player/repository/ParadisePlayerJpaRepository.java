package club.ss220.storage.paradise.spring.jpa.player.repository;

import club.ss220.storage.paradise.spring.jpa.player.entity.ParadisePlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParadisePlayerJpaRepository extends JpaRepository<ParadisePlayerEntity, Integer> {

    Optional<ParadisePlayerEntity> findByCkey(String ckey);
}
