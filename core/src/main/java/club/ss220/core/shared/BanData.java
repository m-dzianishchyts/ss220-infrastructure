package club.ss220.core.shared;

import club.ss220.core.util.StringUtils;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Builder
public record BanData(
        @NotNull
        Integer id,
        @Nullable
        GameServerData server,
        @Nullable
        Integer roundId,
        @NotNull
        @NotBlank
        BanType banType,
        @NotNull
        @NotBlank
        String ckey,
        @NotNull
        @NotBlank
        String adminCkey,
        @NotNull
        @NotBlank
        String reason,
        @Nullable
        String role,
        @NotNull
        @PastOrPresent
        LocalDateTime banDateTime,
        @Nullable
        Duration duration,
        @Nullable
        String unbanAdminCkey,
        @Nullable
        @PastOrPresent
        LocalDateTime unbanDateTime
) {

    public static final int SHORT_REASON_LENGTH = 45;

    @Nullable
    public LocalDateTime expirationDateTime() {
        if (duration != null) {
            return banDateTime.plus(duration);
        }
        return null;
    }

    public boolean isExpired() {
        LocalDateTime expirationDateTime = expirationDateTime();
        return expirationDateTime != null && !expirationDateTime.isAfter(LocalDateTime.now(ZoneOffset.UTC));
    }

    public boolean isUnbanned() {
        return unbanDateTime != null;
    }

    public boolean isActive() {
        return !isExpired() && !isUnbanned();
    }

    public boolean isPermanent() {
        return duration == null || duration.isNegative();
    }

    public String getShortReason() {
        String firstLine = reason.split("\n")[0];
        return StringUtils.truncate(firstLine, SHORT_REASON_LENGTH, "...");
    }

    public enum BanType {
        NORMAL,
        ROLE,
        ADMIN
    }
}
