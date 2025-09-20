package club.ss220.manager.shared.pagination;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;
import java.util.StringJoiner;

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

    @Override
    @NotNull
    public String toString() {
        return new StringJoiner(", ", "[", "]")
                .add("items=" + items.size())
                .add("pageSize=" + pageSize)
                .add("page=" + page)
                .add("startPage=" + startPage)
                .add("endPage=" + endPage)
                .toString();
    }
}
