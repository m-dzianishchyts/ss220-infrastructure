package club.ss220.port.gameserver.tcp.paradise.presentation;

import club.ss220.core.shared.OnlineStaffStatusData;
import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

public record ParadiseOnlineStaffStatusPresentation(
        @NotNull String ckey,
        @NotNull String key,
        @NotNull String rank,
        @NotNull Duration afkDuration,
        @NotNull OnlineStaffStatusData.StealthMode stealthMode,
        @Nullable String stealthKey
) {

    public static final String CKEY_PROPERTY = "ckey";
    public static final String KEY_PROPERTY = "key";
    public static final String RANK_PROPERTY = "rank";
    public static final String AFK_PROPERTY = "afk";
    public static final String STEALTH_PROPERTY = "stealth";
    public static final String STEALTH_KEY_PROPERTY = "stealth_key";

    @JsonCreator
    public ParadiseOnlineStaffStatusPresentation(Map<String, Object> data) {
        this(
                getString(data, CKEY_PROPERTY),
                getString(data, KEY_PROPERTY),
                getString(data, RANK_PROPERTY),
                parseAfk(data),
                parseStealthMode(data),
                (String) data.get(STEALTH_KEY_PROPERTY)
        );
    }

    private static String getString(Map<String, Object> data, String key) {
        return Optional.ofNullable(data.get(key))
                .map(String::valueOf)
                .orElseThrow(() -> propertyNotFound(key));
    }

    private static Duration parseAfk(Map<String, Object> data) {
        return Optional.ofNullable(data.get(AFK_PROPERTY))
                .map(String::valueOf)
                .map(Integer::parseInt)
                .map(deciseconds -> Duration.ofMillis(deciseconds * 100L))
                .orElseThrow(() -> propertyNotFound(AFK_PROPERTY));
    }

    private static OnlineStaffStatusData.StealthMode parseStealthMode(Map<String, Object> data) {
        String value = getString(data, STEALTH_PROPERTY).toLowerCase();
        if (value.contains("stealth")) {
            return OnlineStaffStatusData.StealthMode.STEALTH;
        }
        if (value.contains("bb")) {
            return OnlineStaffStatusData.StealthMode.BIG_BROTHER;
        }
        return OnlineStaffStatusData.StealthMode.NONE;
    }

    private static RuntimeException propertyNotFound(String property) {
        return new IllegalArgumentException("Property not found: " + property);
    }
}
