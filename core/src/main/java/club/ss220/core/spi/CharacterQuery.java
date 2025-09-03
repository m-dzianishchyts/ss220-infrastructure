package club.ss220.core.spi;

import club.ss220.core.shared.GameBuild;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CharacterQuery {

    public static final int MAX_PAGE_SIZE = 500;

    @Nullable
    GameBuild build;
    @Nullable
    String name;
    @Builder.Default
    int page = 0;
    @Builder.Default
    int pageSize = MAX_PAGE_SIZE;
}
