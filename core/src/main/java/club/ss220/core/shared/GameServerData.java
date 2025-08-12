package club.ss220.core.shared;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
public class GameServerData {
    @NotNull
    @NotBlank
    String name;
    @NotNull
    GameBuild build;
    @NotNull
    @NotBlank
    @ToString.Exclude
    String host;
    @NotNull
    @Positive
    @ToString.Exclude
    Integer port;
    @Nullable
    @NotBlank
    @ToString.Exclude
    String key;

    public String fullName() {
        return build.getName() + " " + name;
    }
}
