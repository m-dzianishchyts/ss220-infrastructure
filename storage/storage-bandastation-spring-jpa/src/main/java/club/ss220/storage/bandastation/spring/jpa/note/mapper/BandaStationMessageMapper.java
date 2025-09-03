package club.ss220.storage.bandastation.spring.jpa.note.mapper;

import club.ss220.core.config.GameConfig;
import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.NoteData;
import club.ss220.core.util.InetUtils;
import club.ss220.storage.bandastation.spring.jpa.note.entity.BandaStationMessageEntity;
import lombok.Setter;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.Inet4Address;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class BandaStationMessageMapper {

    @Setter(onMethod_ = @Autowired)
    protected GameConfig gameConfig;

    @Mapping(target = "server", expression = "java(mapServer(messageEntity))")
    @Mapping(target = "editHistory", source = "editHistory", qualifiedByName = "mapEditHistory")
    @Mapping(target = "active", expression = "java(!messageEntity.getDeleted())")
    @Mapping(target = "deactivateCkey", source = "deletedCkey")
    public abstract NoteData toNoteData(BandaStationMessageEntity messageEntity);

    @SneakyThrows
    @Named("mapServer")
    GameServerData mapServer(BandaStationMessageEntity messageEntity) {
        Inet4Address serverIp = InetUtils.fromCompactIPv4(messageEntity.getServerIp());
        return gameConfig.getServerByAddress(serverIp.getHostAddress(), messageEntity.getServerPort()).orElse(null);
    }

    @Named("mapEditHistory")
    String mapEditHistory(String editHistory) {
        return Optional.ofNullable(editHistory)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> s.replaceAll("<(hr|HR)>", "\n").trim())
                .orElse(null);
    }
}
