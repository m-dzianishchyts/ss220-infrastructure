package club.ss220.core.shared.event;

import club.ss220.core.shared.GameServerType;
import jakarta.validation.constraints.NotNull;

public record WhitelistUpdateEvent(
        Action action,
        long guildId,
        @NotNull
        GameServerType serverType,
        long userDiscordId
) {

    public enum Action {
        ADD,
        REMOVE
    }

    public static WhitelistUpdateEvent remove(long guildId, GameServerType serverType, long userDiscordId) {
        return new WhitelistUpdateEvent(Action.REMOVE, guildId, serverType, userDiscordId);
    }

    public static WhitelistUpdateEvent add(long guildId, GameServerType serverType, long userDiscordId) {
        return new WhitelistUpdateEvent(Action.ADD, guildId, serverType, userDiscordId);
    }
}
