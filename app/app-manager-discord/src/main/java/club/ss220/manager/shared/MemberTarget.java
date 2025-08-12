package club.ss220.manager.shared;

import jakarta.validation.constraints.NotNull;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import org.jetbrains.annotations.Nullable;

public record MemberTarget(@Nullable Long discordId, @Nullable String ckey) {

    public static MemberTarget fromUser(User user) {
        return new MemberTarget(user.getIdLong(), null);
    }

    public static MemberTarget fromDiscordId(long discordId) {
        return new MemberTarget(discordId, null);
    }

    public static MemberTarget fromCkey(@NotNull String ckey) {
        return new MemberTarget(null, ckey);
    }

    public String getDisplayString() {
        if (discordId != null) {
            UserSnowflake discordUser = User.fromId(discordId);
            return discordUser.getAsMention();
        } else {
            return ckey;
        }
    }
}
