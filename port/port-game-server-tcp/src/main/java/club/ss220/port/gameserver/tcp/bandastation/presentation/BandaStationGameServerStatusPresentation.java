package club.ss220.port.gameserver.tcp.bandastation.presentation;

import club.ss220.port.gameserver.tcp.support.GameServerResponse;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class BandaStationGameServerStatusPresentation extends GameServerResponse {

    public static final String PLAYERS_PROPERTY = "players";
    public static final String STAFF_PROPERTY = "admins";
    public static final String ROUND_DURATION_PROPERTY = "round_duration";

    public int players() {
        String property = PLAYERS_PROPERTY;
        return Optional.ofNullable(data.get(property))
                .map(String::valueOf)
                .map(Integer::parseInt)
                .orElseThrow(() -> propertyNotFound(property));
    }

    public int staff() {
        String property = STAFF_PROPERTY;
        return Optional.ofNullable(data.get(property))
                .map(String::valueOf)
                .map(Integer::parseInt)
                .orElseThrow(() -> propertyNotFound(property));
    }

    @NotNull
    public Duration roundDuration() {
        String property = ROUND_DURATION_PROPERTY;
        return Optional.ofNullable(data.get(property))
                .map(String::valueOf)
                .map(Integer::parseInt)
                .map(v -> Math.max(v, 0))
                .map(Duration::ofSeconds)
                .orElseThrow(() -> propertyNotFound(property));
    }

    @NotNull
    public Map<String, Object> rawData() {
        return Collections.unmodifiableMap(data);
    }
}
