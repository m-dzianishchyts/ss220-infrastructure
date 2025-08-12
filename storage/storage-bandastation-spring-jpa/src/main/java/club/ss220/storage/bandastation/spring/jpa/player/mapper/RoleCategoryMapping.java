package club.ss220.storage.bandastation.spring.jpa.player.mapper;

import club.ss220.storage.bandastation.spring.jpa.config.BandaStationConfig;
import club.ss220.core.shared.RoleCategory;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class RoleCategoryMapping {

    private final Map<String, RoleCategory> roleCategoryMap;

    public RoleCategoryMapping(BandaStationConfig bandaStationConfig) {
        this.roleCategoryMap = bandaStationConfig.getRoles().entrySet().stream()
                .flatMap(entry -> entry.getValue().stream().map(role -> Map.entry(role, entry.getKey())))
                .map(e -> Map.entry(e.getKey(), RoleCategory.valueOf(e.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public RoleCategory getRoleCategory(String role) {
        return roleCategoryMap.getOrDefault(role, RoleCategory.SPECIAL);
    }

    @Named("mapRoleTime")
    public BandaStationPlayerExperience mapRoleTime(Map<String, Long> roleTime) {
        if (roleTime == null || roleTime.isEmpty()) {
            return new BandaStationPlayerExperience(Map.of());
        }
        Map<RoleCategory, Duration> expMap = roleTime.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> getRoleCategory(e.getKey()),
                        e -> Duration.ofMinutes(e.getValue()),
                        Duration::plus,
                        TreeMap::new
                ));
        return new BandaStationPlayerExperience(expMap);
    }
}
