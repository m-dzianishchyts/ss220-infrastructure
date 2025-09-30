package club.ss220.port.redis.presentation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.Builder;

@Builder
public record RedisIngamePost(
        @JsonProperty("title")
        String title,
        @JsonProperty("channel_name")
        String channelName,
        @JsonProperty("body")
        String body,
        @JsonProperty("author")
        String author,
        @JsonProperty("security_level")
        String securityLevel,
        @JsonProperty("publish_time")
        String publishTime,
        @JsonProperty("server")
        String serverAddress,
        @JsonProperty("round_id")
        long roundId,
        @JsonProperty("author_ckey")
        String authorCkey,
        @Nullable
        @JsonProperty("img")
        String imageBase64
) {
}
