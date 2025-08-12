package club.ss220.port.gameserver.tcp.paradise.mapper;

import club.ss220.port.gameserver.tcp.paradise.presentation.ParadiseOnlineAdminStatusPresentation;
import club.ss220.core.shared.OnlineAdminStatusData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParadiseOnlineAdminStatusMapper {

    @Mapping(source = "rank", target = "ranks", qualifiedByName = "mapRank")
    OnlineAdminStatusData mapToOnlineAdminStatusData(ParadiseOnlineAdminStatusPresentation presentation);

    @Named("mapRank")
    default List<String> mapRank(String rank) {
        return List.of(rank);
    }
}
