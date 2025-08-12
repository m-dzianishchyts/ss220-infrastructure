package club.ss220.storage.central.spring.web.v1.presentation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record UserPresentationV1(
        @JsonProperty("id") int id,
        @JsonProperty("ckey") @NotNull String ckey,
        @JsonProperty("discord_id") @NotNull String discordId
) {
}
