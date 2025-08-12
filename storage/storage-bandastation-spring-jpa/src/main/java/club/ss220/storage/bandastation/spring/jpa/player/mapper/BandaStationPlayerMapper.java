package club.ss220.storage.bandastation.spring.jpa.player.mapper;

import club.ss220.storage.bandastation.spring.jpa.player.entity.BandaStationPlayerEntity;
import club.ss220.core.shared.PlayerData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

@Mapper(componentModel = "spring", uses = RoleCategoryMapping.class)
public interface BandaStationPlayerMapper {

    @Mapping(target = "gameBuild", expression = "java(club.ss220.core.shared.GameBuild.BANDASTATION)")
    @Mapping(target = "firstSeenDateTime", source = "firstSeen")
    @Mapping(target = "lastSeenDateTime", source = "lastSeen")
    @Mapping(target = "ip", source = "ip", qualifiedByName = "ipToInet")
    @Mapping(target = "exp", source = "roleTime", qualifiedByName = "mapRoleTime")
    @Mapping(target = "characters", ignore = true)
    PlayerData toPlayerData(BandaStationPlayerEntity player);

    @Named("ipToInet")
    default InetAddress ipToInet(long ip) {
        // DB column is UNSIGNED INT (32 bits). Stored as long in entity cause int is not enough.
        int value = (int) ip;
        byte[] bytes = ByteBuffer.allocate(Integer.BYTES).putInt(value).array();
        try {
            return InetAddress.getByAddress(bytes);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Invalid IP value: " + Integer.toHexString(value), e);
        }
    }
}
