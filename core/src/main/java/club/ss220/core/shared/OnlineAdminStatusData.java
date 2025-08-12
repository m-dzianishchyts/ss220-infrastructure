package club.ss220.core.shared;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.Duration;
import java.util.List;

public record OnlineAdminStatusData(
        @NotNull
        @NotBlank
        String ckey,
        @NotNull
        @NotBlank
        String key,
        @NotNull
        @NotEmpty
        List<String> ranks,
        @NotNull
        Duration afkDuration,
        @NotNull
        StealthMode stealthMode,
        @Nullable
        String stealthKey
) {

    @Getter
    public enum StealthMode {
        NONE,
        STEALTH,
        BIG_BROTHER
    }
}
