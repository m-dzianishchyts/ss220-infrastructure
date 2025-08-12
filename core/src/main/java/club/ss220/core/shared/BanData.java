package club.ss220.core.shared;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Builder
public record BanData(
        @NotNull
        Integer id,
        @NotNull
        @NotBlank
        String ckey,
        @NotNull
        @NotBlank
        String adminCkey,
        @NotNull
        @NotBlank
        String reason,
        @NotNull
        @PastOrPresent
        LocalDateTime banTime,
        @Nullable
        LocalDateTime unbanTime,
        @NotNull
        @NotBlank
        String banType,
        @NotNull
        Boolean isActive
) {
    public static final int SHORT_REASON_LENGTH = 45;

    public boolean isExpired() {
        return unbanTime != null && unbanTime.isBefore(LocalDateTime.now(ZoneOffset.UTC));
    }

    public String getShortReason() {
        String firstLine = reason.split("\n")[0];
        return StringUtils.truncate(firstLine, SHORT_REASON_LENGTH);
    }
}
