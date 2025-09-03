package club.ss220.core.spi;

import club.ss220.core.shared.GameServerData;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NoteQuery {

    public static final int MAX_PAGE_SIZE = 100;

    @Nullable
    String ckey;
    @Nullable
    String adminCkey;
    @Nullable
    GameServerData server;
    @Nullable
    Integer roundId;
    @Nullable
    Boolean active;
    @Builder.Default
    int page = 0;
    @Builder.Default
    int pageSize = MAX_PAGE_SIZE;
}
