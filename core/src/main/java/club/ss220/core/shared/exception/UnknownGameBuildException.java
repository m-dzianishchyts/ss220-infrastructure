package club.ss220.core.shared.exception;

import lombok.Getter;

@Getter
public class UnknownGameBuildException extends DomainException {

    private final String serverName;

    public UnknownGameBuildException(String serverName) {
        super("Unknown game build: " + serverName);
        this.serverName = serverName;
    }
}
