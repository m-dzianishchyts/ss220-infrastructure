package club.ss220.storage.paradise.spring.jpa.ban.mapper;

import club.ss220.core.config.GameConfig;
import club.ss220.core.shared.BanData;
import club.ss220.core.shared.GameServerData;
import club.ss220.storage.paradise.spring.jpa.ban.entity.ParadiseBanEntity;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class ParadiseBanMapper {

    @Setter(onMethod_ = @Autowired)
    protected GameConfig gameConfig;

    @Mapping(target = "server", expression = "java(mapServer(banEntity))")
    @Mapping(target = "role", source = "role", qualifiedByName = "mapRole")
    @Mapping(target = "duration", source = "duration", qualifiedByName = "mapDuration")
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
        return optionalServer.orElseThrow(() -> serverResolutionError(banEntity));
    }

    @Named("mapRole")
    String mapRole(String role) {
        return Optional.ofNullable(role).map(String::trim).orElse(null);
    }

    @Named("mapDuration")
    Duration mapDuration(Integer minutes) {
        return Optional.of(minutes).filter(m -> m >= 0).map(Duration::ofMinutes).orElse(null);
    }

    private RuntimeException serverResolutionError(ParadiseBanEntity banEntity) {
        return new IllegalArgumentException("Couldn't resolve server for ban: " + banEntity);
    }
}
