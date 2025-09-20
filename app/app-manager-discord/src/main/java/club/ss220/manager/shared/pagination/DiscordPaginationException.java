package club.ss220.manager.shared.pagination;

public class DiscordPaginationException extends RuntimeException {

    public DiscordPaginationException(String message) {
        super(message);
    }

    public DiscordPaginationException(String message, Throwable cause) {
        super(message, cause);
    }
}
