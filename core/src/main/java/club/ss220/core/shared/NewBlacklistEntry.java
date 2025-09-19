package club.ss220.core.shared;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record NewBlacklistEntry(
        long playerDiscordId,
        long adminDiscordId,
        @NotBlank String serverType,
        @Min(1) int durationDays,
        @Nullable String reason,
        @Nullable Boolean invalidateWhitelist
) {
}
