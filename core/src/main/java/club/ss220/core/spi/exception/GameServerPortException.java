package club.ss220.core.spi.exception;

import club.ss220.core.shared.GameServerData;

public class GameServerPortException extends RuntimeException {

    private final GameServerData server;

    public GameServerPortException(GameServerData server, String msg) {
        super(msg);
        this.server = server;
    }

    public GameServerPortException(GameServerData server, String msg, Throwable cause) {
        super(msg, cause);
        this.server = server;
    }
}
