package club.ss220.storage.paradise.spring.jpa.ban.mapper;

import club.ss220.core.config.GameConfig;
import club.ss220.core.shared.BanData;
import club.ss220.core.shared.GameServerData;
import club.ss220.storage.paradise.spring.jpa.ban.entity.ParadiseBanEntity;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Mapper(componentModel = "spring")
public abstract class ParadiseBanMapper {

    @Setter(onMethod_ = @Autowired)
    protected GameConfig gameConfig;

    @Mapping(target = "server", expression = "java(mapServer(banEntity))")
    @Mapping(target = "role", source = "role", qualifiedByName = "mapRole")
    @Mapping(target = "duration", source = "duration", qualifiedByName = "mapDuration")
    @Mapping(target = "editHistory", source = "editHistory", qualifiedByName = "mapEditHistory")
    public abstract BanData toBanData(ParadiseBanEntity banEntity);

    @Named("mapServer")
    GameServerData mapServer(ParadiseBanEntity banEntity) {
        String[] address = banEntity.getServerAddress().split(":");
        String host = address[0];
        int port = Integer.parseInt(address[1]);
        Optional<GameServerData> optionalServer = gameConfig.getServerByAddress(host, port);
        if (optionalServer.isEmpty()) {
            optionalServer = gameConfig.getServerById(banEntity.getServerId());
        }
        return optionalServer.orElseGet(() -> {
            log.warn("Couldn't resolve server for ban {}", banEntity);
            return null;
        });
    }

    @Named("mapRole")
    String mapRole(String role) {
        return Optional.ofNullable(role).map(String::trim).filter(s -> !s.isBlank()).orElse(null);
    }

    @Named("mapDuration")
    Duration mapDuration(Integer minutes) {
        return Optional.of(minutes).filter(m -> m >= 0).map(Duration::ofMinutes).orElse(null);
    }

    @Named("mapEditHistory")
    String mapEditHistory(String editHistory) {
        return Optional.ofNullable(editHistory)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> s.replaceAll("<(br|BR)>", "\n").trim())
                .orElse(null);
    }
}
