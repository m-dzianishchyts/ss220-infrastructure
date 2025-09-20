package club.ss220.manager.shared.events;

import club.ss220.manager.shared.GameServerType;
import net.dv8tion.jda.api.entities.Guild;

public record WhitelistUpdateEvent(Action action, Guild guild, GameServerType serverType, long userDiscordId) {

    public enum Action {
        ADD,
        REMOVE
    }

    public static WhitelistUpdateEvent remove(Guild guild, GameServerType serverType, long userDiscordId) {
        return new WhitelistUpdateEvent(Action.REMOVE, guild, serverType, userDiscordId);
    }

    public static WhitelistUpdateEvent add(Guild guild, GameServerType serverType, long userDiscordId) {
        return new WhitelistUpdateEvent(Action.ADD, guild, serverType, userDiscordId);
    }
}
