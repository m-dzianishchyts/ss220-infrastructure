package club.ss220.core.shared;

import jakarta.validation.constraints.NotNull;

import java.util.NavigableMap;

public record MemberData(
        @NotNull
        UserData userData,
        @NotNull
        NavigableMap<GameBuild, PlayerData> gameInfo
) {
}
