package club.ss220.manager.shared.pagination;

import io.github.freya022.botcommands.api.components.event.ButtonEvent;
import io.github.freya022.botcommands.api.components.event.StringSelectEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenericPaginationController {

    private static final int PAGE_LIMIT = SelectMenu.OPTIONS_MAX_AMOUNT;

    private final GenericPaginationView view;

    public <T> void show(IReplyCallback interaction, List<T> items, int pageSize, PageRenderer<T> renderer) {
        int currentPage = 0;
        int totalPages = Math.max(1, (int) Math.ceil((double) items.size() / pageSize));
        PageWindow pageWindow = computeWindow(currentPage, totalPages, PAGE_LIMIT);

        PaginatedContext<T> ctx = PaginatedContext.<T>builder()
                .controller(this)
                .items(items)
                .pageSize(pageSize)
                .page(currentPage)
                .startPage(pageWindow.start())
                .endPage(pageWindow.end())
                .build();
        view.renderFirst(interaction.getHook(), ctx, renderer);
    }

    public <T> void onPageSelected(StringSelectEvent event, PaginatedContext<T> ctx, PageRenderer<T> renderer) {
        PaginatedContext<T> newCtx = null;
        try {
            event.deferEdit().queue();
            int newPage = Integer.parseInt(event.getValues().getFirst());
            newCtx = contextWithWindow(ctx.withPage(newPage));
            view.update(event.getHook(), newCtx, renderer);
        } catch (Exception e) {
            String message = "Failed to switch page, context = " + ctx + ", new context = " + newCtx;
            throw new DiscordPaginationException(message, e);
        }
    }

    public <T> void onItemSelected(StringSelectEvent event, PaginatedContext<T> ctx, PageRenderer<T> renderer) {
        Integer index = null;
        try {
            event.deferEdit().queue();
            index = Integer.parseInt(event.getValues().getFirst());
            T item = ctx.pageItems().get(index);
            view.updateToDetails(event.getHook(), item, ctx, renderer);
        } catch (Exception e) {
            String message = "Failed to show item details, context = " + ctx + ", index = " + index;
            throw new DiscordPaginationException(message, e);
        }
    }

    public <T> void onBack(ButtonEvent event, PaginatedContext<T> ctx, PageRenderer<T> renderer) {
        try {
            event.deferEdit().queue();
            view.update(event.getHook(), ctx, renderer);
        } catch (Exception e) {
            String message = "Failed to return to list, context = " + ctx;
            throw new DiscordPaginationException(message, e);
        }
    }

    private <T> PaginatedContext<T> contextWithWindow(PaginatedContext<T> ctx) {
        int totalPages = Math.max(1, ctx.totalPages());
        int page = Math.clamp(ctx.page(), 0, totalPages - 1);
        int limit = Math.min(PAGE_LIMIT, totalPages);
        PageWindow pageWindow = computeWindow(page, totalPages, limit);
        return ctx.withPageWindow(pageWindow.start, pageWindow.end);
    }

    private record PageWindow(int start, int end) {}

    private PageWindow computeWindow(int currentPage, int totalPages, int limit) {
        if (totalPages <= limit) {
            return new PageWindow(0, totalPages - 1);
        }

        int pagesBefore = (int) Math.ceil(limit / 2.0 - 1);
        int start = Math.max(0, currentPage - pagesBefore);
        int end = Math.min(totalPages - 1, start + limit - 1);
        start = Math.max(0, end - (limit - 1));
        return new PageWindow(start, end);
    }
}
