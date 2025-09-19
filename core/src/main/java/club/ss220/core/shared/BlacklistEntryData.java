package club.ss220.core.shared;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record BlacklistEntryData(
        @NotNull
        Integer id,
        @NotNull
        Integer playerId,
        @Nullable
        UserData playerData,
        @NotNull
        Integer adminId,
        @Nullable
        UserData adminData,
        @NotBlank
        String serverType,
        @NotNull
        @PastOrPresent
        LocalDateTime issueDateTime,
        @NotNull
        LocalDateTime expirationDateTime,
        @NotNull
        Boolean active,
        @Nullable
        String reason) {
}
