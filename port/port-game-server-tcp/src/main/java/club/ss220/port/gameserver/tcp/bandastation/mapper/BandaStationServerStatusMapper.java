package club.ss220.port.gameserver.tcp.bandastation.mapper;

import club.ss220.core.shared.GameServerStatusData;
import club.ss220.port.gameserver.tcp.bandastation.presentation.BandaStationGameServerStatusPresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BandaStationServerStatusMapper {

    @Mapping(target = "players", expression = "java(presentation.players())")
    @Mapping(target = "admins", expression = "java(presentation.admins())")
    @Mapping(target = "roundDuration", expression = "java(presentation.roundDuration())")
    @Mapping(target = "rawData", expression = "java(presentation.rawData())")
    GameServerStatusData toGameServerStatusData(BandaStationGameServerStatusPresentation presentation);
}
