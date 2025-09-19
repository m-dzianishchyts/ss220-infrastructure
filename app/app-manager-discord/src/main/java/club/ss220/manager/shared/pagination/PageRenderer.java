package club.ss220.manager.shared.pagination;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ActionRow;

import java.util.List;

public interface PageRenderer<T> {
    
    MessageEmbed renderPage(PaginatedContext<T> ctx);

    default String paginationFooter(PaginatedContext<T> ctx) {
        return "Всего: " + ctx.totalItems() + " — Страница " + (ctx.page() + 1) + "/" + ctx.totalPages();
    }

    default void enrichComponents(PaginatedContext<T> ctx, List<ActionRow> rows) {
        // override to inject additional components
    }

    default boolean enableItemSelector() { return false; }

    default String itemOptionLabel(T item) { return String.valueOf(item); }

    default MessageEmbed renderDetails(T item) {
        throw new UnsupportedOperationException("renderDetails is not implemented");
    }
}
