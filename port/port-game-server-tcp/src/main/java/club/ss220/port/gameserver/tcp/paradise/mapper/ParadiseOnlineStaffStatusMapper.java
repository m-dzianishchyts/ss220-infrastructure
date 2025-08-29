package club.ss220.port.gameserver.tcp.paradise.mapper;

import club.ss220.core.shared.OnlineStaffStatusData;
import club.ss220.port.gameserver.tcp.paradise.presentation.ParadiseOnlineStaffStatusPresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParadiseOnlineStaffStatusMapper {

    @Mapping(source = "rank", target = "ranks", qualifiedByName = "mapRank")
    OnlineStaffStatusData toOnlineStaffStatusData(ParadiseOnlineStaffStatusPresentation presentation);

    @Named("mapRank")
    default List<String> mapRank(String rank) {
        return List.of(rank);
    }
}
