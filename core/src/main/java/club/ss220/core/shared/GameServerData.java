package club.ss220.core.shared;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.net.Inet4Address;

@Data
@Accessors(fluent = true)
public class GameServerData {

    @Nullable
    private final String id;

    @NotBlank
    private final String name;

    @NotNull
    private final GameBuild build;

    @NotNull
    private final Boolean active;

    @ToString.Exclude
    @Nullable
    private final String host;

    @ToString.Exclude
    private Inet4Address ip;

    @ToString.Exclude
    @NotNull
    @Positive
    private final Integer port;

    @ToString.Exclude
    @Nullable
    private final String key;

    public GameServerData(@Nullable String id, @NotNull String name, @NotNull GameBuild build,
                          @DefaultValue("true") @NotNull Boolean active,
                          @Nullable String host, @Nullable Inet4Address ip, @NotNull Integer port,
                          @Nullable String key) {
        this.id = id;
        this.name = name;
        this.build = build;
        this.active = active;
        this.host = host;
//        this.ip = ip != null ? Inet4Address.ofLiteral(ip) : null;
        this.ip = ip;
        this.port = port;
        this.key = key;
    }

    public String fullName() {
        return build.getName() + " " + name;
    }
}
