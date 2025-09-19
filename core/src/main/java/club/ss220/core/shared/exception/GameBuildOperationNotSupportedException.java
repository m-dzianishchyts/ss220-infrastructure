package club.ss220.core.shared.exception;

import club.ss220.core.shared.GameBuild;
import lombok.Getter;

@Getter
public class GameBuildOperationNotSupportedException extends DomainException {

    private final GameBuild gameBuild;

    public GameBuildOperationNotSupportedException(GameBuild gameBuild, String message) {
        super(message);
        this.gameBuild = gameBuild;
    }

    public GameBuildOperationNotSupportedException(GameBuild gameBuild, String message, Throwable cause) {
        super(message, cause);
        this.gameBuild = gameBuild;
    }
}
