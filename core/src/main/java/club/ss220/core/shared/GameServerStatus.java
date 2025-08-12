package club.ss220.core.shared;

import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.util.Map;

@Deprecated
public interface GameServerStatus {

    @NotNull
    Integer getPlayers();

    @NotNull
    Integer getAdmins();

    @NotNull
    Duration getRoundDuration();

    @NotNull
    Map<String, Object> getRawData();
}
