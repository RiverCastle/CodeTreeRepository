package com.codetree.CodeTreeHRM.hr.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PageResponse<T> {
    private final List<T> content;
    private final long totalCount;
    private final int page;
    private final int size;
    private final int totalPages;

    private PageResponse(List<T> content, long totalCount, int page, int size) {
        this.content = content;
        this.totalCount = totalCount;
        this.page = page;
        this.size = size;
        this.totalPages = size > 0 ? (int) Math.ceil((double) totalCount / size) : 0;
    }

    public static <T> PageResponse<T> of(List<T> content, long totalCount, int page, int size) {
        return new PageResponse<>(content, totalCount, page, size);
    }
}
