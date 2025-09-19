package club.ss220.storage.central.spring.web.v1.blacklist.presentation;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BlacklistPresentationV1(
        @JsonProperty("id") Integer id,
        @JsonProperty("player_id") Integer playerId,
        @JsonProperty("admin_id") Integer adminId,
        @JsonProperty("server_type") String serverType,
        @JsonProperty("issue_time") String issueDateTime,
        @JsonProperty("expiration_time") String expirationDateTime,
        @JsonProperty("valid") Boolean active,
        @JsonProperty("reason") String reason
) {
}
