package club.ss220.port.gameserver.tcp.paradise.presentation;

import club.ss220.port.gameserver.tcp.support.GameServerResponse;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class ParadiseGameServerStatusPresentation extends GameServerResponse {

    public static final String PLAYERS_PROPERTY = "players";
    public static final String STAFF_PROPERTY = "admins";
    public static final String ROUNDTIME_PROPERTY = "roundtime";

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
        String property = ROUNDTIME_PROPERTY;
        return Optional.ofNullable(data.get(property))
                .map(String::valueOf)
                .map(ParadiseGameServerStatusPresentation::parseDuration)
                .orElseThrow(() -> propertyNotFound(property));
    }

    @NotNull
    public Map<String, Object> rawData() {
        return Collections.unmodifiableMap(data);
    }

    private static Duration parseDuration(String duration) {
        String[] parts = duration.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);
        return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
    }
}
