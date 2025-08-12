package club.ss220.manager.shared.pagination;

public interface PaginatedQuery<Q extends PaginatedQuery<Q>> {

    int getLimit();

    int getPage();

    Q withPage(int page);
}
