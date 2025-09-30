package club.ss220.core.shared;

import jakarta.validation.constraints.NotBlank;

public record GameServerType(@NotBlank String name) {

    public static GameServerType fromName(String name) {
        return new GameServerType(name);
    }
}
