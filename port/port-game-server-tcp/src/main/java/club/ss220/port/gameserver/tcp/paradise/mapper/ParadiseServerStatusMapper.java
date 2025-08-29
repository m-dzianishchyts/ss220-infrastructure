package club.ss220.port.gameserver.tcp.paradise.mapper;

import club.ss220.core.shared.GameServerStatusData;
import club.ss220.port.gameserver.tcp.paradise.presentation.ParadiseGameServerStatusPresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParadiseServerStatusMapper {

    @Mapping(target = "players", expression = "java(presentation.players())")
    @Mapping(target = "admins", expression = "java(presentation.admins())")
    @Mapping(target = "rawData", expression = "java(presentation.rawData())")
    @Mapping(target = "roundDuration", expression = "java(presentation.roundDuration())")
    GameServerStatusData toGameServerStatusData(ParadiseGameServerStatusPresentation presentation);
}
