package club.ss220.storage.bandastation.spring.jpa.player.mapper;

import club.ss220.core.shared.PlayerExperienceData;
import club.ss220.core.shared.RoleCategory;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class BandaStationPlayerExperience extends PlayerExperienceData {

    public BandaStationPlayerExperience(Map<RoleCategory, Duration> exp) {
        super(exp);
    }

    @Override
    public List<RoleCategory> getRelevantRoles() {
        return List.of(
                RoleCategory.ADMIN,
                RoleCategory.GHOST,
                RoleCategory.LIVING,
                RoleCategory.SPECIAL,
                RoleCategory.ANTAGONIST,
                RoleCategory.CREW,
                RoleCategory.COMMAND,
                RoleCategory.NT_REPRESENTATION,
                RoleCategory.SECURITY,
                RoleCategory.ENGINEERING,
                RoleCategory.SCIENCE,
                RoleCategory.MEDICAL,
                RoleCategory.SUPPLY,
                RoleCategory.SERVICE,
                RoleCategory.JUSTICE,
                RoleCategory.MISC,
                RoleCategory.SILICON
        );
    }
}
