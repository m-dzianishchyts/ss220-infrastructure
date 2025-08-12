package club.ss220.core.shared;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.time.Duration;
import java.util.Map;

@Builder
public record GameServerStatusData(
        @NotNull
        @PositiveOrZero
        Integer players,
        @NotNull
        @PositiveOrZero
        Integer admins,
        @NotNull
        Duration roundDuration,
        @NotNull
        Map<String, Object> rawData
) {
}
