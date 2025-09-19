package club.ss220.core.spi;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BlacklistQuery {

    public static final int MAX_PAGE_SIZE = 100;

    @Nullable
    Long playerDiscordId;
    @Nullable
    Long adminDiscordId;
    @Nullable
    String serverType;
    @Nullable
    Boolean activeOnly;
    @Builder.Default
    int page = 0;
    @Builder.Default
    int pageSize = MAX_PAGE_SIZE;
}
