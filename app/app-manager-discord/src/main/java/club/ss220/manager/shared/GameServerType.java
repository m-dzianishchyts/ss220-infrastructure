package club.ss220.manager.shared;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record GameServerType(
        @NotBlank
        String name,
        @Nullable
        Long discordRoleId) {
}
