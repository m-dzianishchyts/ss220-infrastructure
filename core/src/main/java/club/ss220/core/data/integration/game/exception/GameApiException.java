package club.ss220.core.data.integration.game.exception;

import club.ss220.core.shared.GameServer;
import org.springframework.dao.DataAccessException;

@Deprecated
public class GameApiException extends DataAccessException {

    private final GameServer server;

    public GameApiException(GameServer server, String msg) {
        super(msg);
        this.server = server;
    }

    public GameApiException(GameServer server, String msg, Throwable cause) {
        super(msg, cause);
        this.server = server;
    }
}
