package club.ss220.core.shared;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NoteData(
        @NotNull
        Integer id,
        @Nullable
        GameServerData server,
        @Nullable
        Integer roundId,
        @NotEmpty
        String ckey,
        @NotEmpty
        String adminCkey,
        @NotNull
        LocalDateTime timestamp,
        @NotEmpty
        String text,
        @Nullable
        String editHistory,
        @NotNull
        Boolean active,
        @Nullable
        String deactivateCkey
) {
}
