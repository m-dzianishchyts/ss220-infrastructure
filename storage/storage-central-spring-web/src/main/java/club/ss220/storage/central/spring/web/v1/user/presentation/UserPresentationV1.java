package club.ss220.storage.central.spring.web.v1.user.presentation;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserPresentationV1(
        @JsonProperty("id") int id,
        @JsonProperty("ckey") String ckey,
        @JsonProperty("discord_id") String discordId
) {
}
