package club.ss220.manager.config;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record GameServerTypeConfig(@NotBlank String name, @Nullable Long discordRoleId) {
}
