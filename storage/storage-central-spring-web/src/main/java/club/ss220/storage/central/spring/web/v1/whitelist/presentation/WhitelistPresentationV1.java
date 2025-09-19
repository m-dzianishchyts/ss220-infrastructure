package club.ss220.storage.central.spring.web.v1.whitelist.presentation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record WhitelistPresentationV1(
        @JsonProperty("id") Integer id,
        @JsonProperty("player_id") Integer playerId,
        @JsonProperty("server_type") String serverType,
        @JsonProperty("admin_id") Integer adminId,
        @JsonProperty("issue_time") LocalDateTime issueDateTime,
        @JsonProperty("expiration_time") LocalDateTime expirationDateTime,
        @JsonProperty("valid") Boolean active
) {
}
