package club.ss220.core.shared.exception;

import lombok.Getter;

@Getter
public class UserBlacklistedException extends DomainException {

    private final Long discordId;
    private final String ckey;

    public UserBlacklistedException(long discordId) {
        super("User with discordId " + discordId + " is blacklisted.");
        this.discordId = discordId;
        ckey = null;
    }

    public UserBlacklistedException(long discordId, Throwable cause) {
        super("User with discordId " + discordId + " is blacklisted.", cause);
        this.discordId = discordId;
        ckey = null;
    }

    public UserBlacklistedException(String ckey) {
        super("User with ckey " + ckey + " is blacklisted.");
        this.ckey = ckey;
        discordId = null;
    }

    public UserBlacklistedException(String ckey, Throwable cause) {
        super("User with ckey " + ckey + " is blacklisted.", cause);
        this.ckey = ckey;
        discordId = null;
    }
}
