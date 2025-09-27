package club.ss220.core.shared;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GameBuildData {

    private final boolean enabled;

    public static GameBuildData enabled() {
        return new GameBuildData(true);
    }

    public static GameBuildData disabled() {
        return new GameBuildData(false);
    }
}
