package club.ss220.port.gameserver.tcp.bandastation.mapper;

import club.ss220.core.shared.OnlineStaffStatusData;
import club.ss220.port.gameserver.tcp.bandastation.presentation.BandaStationOnlineStaffStatusPresentation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BandaStationOnlineStaffStatusMapper {

    OnlineStaffStatusData toOnlineStaffStatusData(BandaStationOnlineStaffStatusPresentation presentation);
}
