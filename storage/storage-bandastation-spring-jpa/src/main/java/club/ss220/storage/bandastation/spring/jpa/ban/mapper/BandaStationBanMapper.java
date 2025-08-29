package club.ss220.storage.bandastation.spring.jpa.ban.mapper;

import club.ss220.core.config.GameConfig;
import club.ss220.core.shared.BanData;
import club.ss220.core.shared.GameServerData;
import club.ss220.core.util.InetUtils;
import club.ss220.storage.bandastation.spring.jpa.ban.entity.BandaStationBanEntity;
import lombok.Setter;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.Inet4Address;
import java.time.Duration;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class BandaStationBanMapper {

    @Setter(onMethod_ = @Autowired)
    protected GameConfig gameConfig;

    @Mapping(target = "server", expression = "java(mapServer(banEntity))")
    @Mapping(target = "role", source = "role", qualifiedByName = "mapRole")
    @Mapping(target = "duration", expression = "java(getDuration(banEntity))")
    public abstract BanData toBanData(BandaStationBanEntity banEntity);

    @SneakyThrows
    @Named("mapServer")
    GameServerData mapServer(BandaStationBanEntity banEntity) {
        Inet4Address serverIp = InetUtils.fromCompactIPv4(banEntity.getServerIp());
        return gameConfig.getServerByAddress(serverIp.getHostAddress(), banEntity.getServerPort()).orElse(null);
    }

    @Named("mapRole")
    String mapRole(String role) {
        return Optional.ofNullable(role)
                .map(String::trim)
                .filter(r -> !r.equalsIgnoreCase("server"))
                .orElse(null);
    }

    @Named("getDuration")
    Duration getDuration(BandaStationBanEntity banEntity) {
        return Optional.ofNullable(banEntity.getExpirationDateTime())
                .map(dt -> Duration.between(banEntity.getBanDateTime(), dt))
                .filter(Duration::isPositive)
                .orElse(null);
    }
}
