package club.ss220.storage.central.spring.web.v1.whitelist.presentation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record NewWhitelistDiscordV1(
        @NotBlank
        @JsonProperty("server_type")
        String serverType,
        @NotBlank
        @JsonProperty("player_discord_id")
        String playerDiscordId,
        @NotBlank
        @JsonProperty("admin_discord_id")
        String adminDiscordId,
        @JsonProperty("duration_days")
        int durationDays
) {
}
