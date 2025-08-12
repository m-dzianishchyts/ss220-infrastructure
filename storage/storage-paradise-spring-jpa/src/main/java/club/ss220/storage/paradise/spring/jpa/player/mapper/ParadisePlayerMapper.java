package club.ss220.storage.paradise.spring.jpa.player.mapper;

import club.ss220.storage.paradise.spring.jpa.character.mapper.ParadiseCharacterMapper;
import club.ss220.storage.paradise.spring.jpa.player.entity.ParadisePlayerEntity;
import club.ss220.core.shared.PlayerData;
import club.ss220.core.shared.PlayerExperienceData;
import club.ss220.core.shared.RoleCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Arrays;
import java.util.TreeMap;

@Mapper(componentModel = "spring", uses = {ParadiseCharacterMapper.class})
public interface ParadisePlayerMapper {

    @Mapping(target = "gameBuild", expression = "java(club.ss220.core.shared.GameBuild.PARADISE)")
    @Mapping(target = "firstSeenDateTime", source = "firstSeen")
    @Mapping(target = "lastSeenDateTime", source = "lastSeen")
    @Mapping(target = "ip", source = "ip", qualifiedByName = "ipToInet")
    @Mapping(target = "exp", source = "exp", qualifiedByName = "mapRoleTime")
    PlayerData toPlayerData(ParadisePlayerEntity player);

    @Named("ipToInet")
    default InetAddress ipToInet(String ip) {
        try {
            return InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Invalid IP value: " + ip, e);
        }
    }

    @Named("mapRoleTime")
    default PlayerExperienceData parseParadiseExp(String exp) {
        TreeMap<RoleCategory, Duration> playerExp = new TreeMap<>();
        if (exp != null && !exp.isBlank()) {
            Arrays.stream(exp.split("&"))
                    .map(v -> v.split("="))
                    .forEach(v -> playerExp.merge(
                            RoleCategory.fromValue(v[0]),
                            Duration.ofMinutes(Integer.parseInt(v[1])),
                            Duration::plus));
        }
        return new ParadisePlayerExperience(playerExp);
    }
}
