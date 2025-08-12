package club.ss220.storage.paradise.spring.jpa.player.mapper;

import club.ss220.core.shared.PlayerExperienceData;
import club.ss220.core.shared.RoleCategory;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class ParadisePlayerExperience extends PlayerExperienceData {

    public ParadisePlayerExperience(Map<RoleCategory, Duration> exp) {
        super(exp);
    }

    @Override
    public List<RoleCategory> getRelevantRoles() {
        return List.of(
                RoleCategory.GHOST,
                RoleCategory.LIVING,
                RoleCategory.SPECIAL,
                RoleCategory.CREW,
                RoleCategory.COMMAND,
                RoleCategory.SECURITY,
                RoleCategory.ENGINEERING,
                RoleCategory.SCIENCE,
                RoleCategory.MEDICAL,
                RoleCategory.SUPPLY,
                RoleCategory.SERVICE,
                RoleCategory.MISC,
                RoleCategory.SILICON
        );
    }
}
