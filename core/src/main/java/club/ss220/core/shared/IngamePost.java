package club.ss220.core.shared;

import jakarta.annotation.Nullable;
import lombok.Builder;

import java.awt.image.BufferedImage;
import java.time.LocalTime;

@Builder
public record IngamePost(
        String title,
        String channelName,
        String body,
        String author,
        String securityLevel,
        LocalTime publishTime,
        String serverHost,
        int serverPort,
        long roundId,
        String authorCkey,
        @Nullable
        BufferedImage image
) {
}
