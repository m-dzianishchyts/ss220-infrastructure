package club.ss220.storage.paradise.spring.jpa.note.mapper;

import club.ss220.core.config.GameConfig;
import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.NoteData;
import club.ss220.storage.paradise.spring.jpa.note.entity.ParadiseNoteEntity;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class ParadiseNoteMapper {

    @Setter(onMethod_ = @Autowired)
    protected GameConfig gameConfig;

    @Mapping(target = "server", source = "serverId", qualifiedByName = "mapServer")
    @Mapping(target = "editHistory", source = "editHistory", qualifiedByName = "mapEditHistory")
    @Mapping(target = "active", expression = "java(!noteEntity.getDeleted())")
    @Mapping(target = "deactivateCkey", source = "deletedCkey")
    public abstract NoteData toNoteData(ParadiseNoteEntity noteEntity);

    @Named("mapServer")
    GameServerData mapServer(String serverId) {
        return Optional.ofNullable(serverId).flatMap(gameConfig::getServerById).orElse(null);
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
