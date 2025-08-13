package club.ss220.manager.shared.pagination;

import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record PaginatedContext<T>(
        GenericPaginationController controller,
        List<T> items,
        int pageSize,
        int page,
        int startPage,
        int endPage
) {

    public int totalItems() {
        return items.size();
    }

    public int totalPages() {
        return (int) Math.ceil((double) items.size() / pageSize);
    }

    public List<T> pageItems() {
        return items.subList(page * pageSize, Math.min(items.size(), (page + 1) * pageSize));
    }

    public PaginatedContext<T> withPage(int newPage) {
        return toBuilder().page(newPage).build();
    }

    public PaginatedContext<T> withPageWindow(int start, int end) {
        return toBuilder().startPage(start).endPage(end).build();
    }
}
