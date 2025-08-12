package club.ss220.core.shared;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UserData(
        @NotNull Integer id,
        @NotNull @NotBlank String ckey,
        @NotNull @Positive Long discordId
) {
}
