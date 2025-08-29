package club.ss220.port.gameserver.tcp.bandastation.mapper;

import club.ss220.core.shared.OnlineAdminStatusData;
import club.ss220.port.gameserver.tcp.bandastation.presentation.BandaStationOnlineAdminStatusPresentation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BandaStationOnlineAdminStatusMapper {

    OnlineAdminStatusData toOnlineAdminStatusData(BandaStationOnlineAdminStatusPresentation presentation);
}
