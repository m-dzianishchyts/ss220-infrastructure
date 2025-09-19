package club.ss220.storage.central.spring.web.v1.presentation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PaginatedResponseV1<T>(
        @JsonProperty("items") List<T> items,
        @JsonProperty("total") int total,
        @JsonProperty("page") int page,
        @JsonProperty("page_size") int pageSize,
        @JsonProperty("next_page") Integer nextPage,
        @JsonProperty("previous_page") Integer previousPage,
        @JsonProperty("next_page_path") String nextPagePath,
        @JsonProperty("previous_page_path") String previousPagePath
) {
}
