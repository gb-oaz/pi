package com.pi.utils.models;

import java.util.List;

public class Pageable<T> {
    private List<T> content;
    private Pagination pagination;

    public Pageable<T> builder() { return new Pageable<T>(); }

    public Pageable<T> content(List<T> content) { this.content = content; return this; }
    public Pageable<T> pagination(Pagination pagination) { this.pagination = pagination; return this; }

    public Pageable<T> build() { return this; }

    // Getters
    public List<T> getContent() { return content; }
    public Pagination getPagination() { return pagination; }

    public static class Pagination {
        private int page;
        private int size;
        private int total;

        public Pagination builder() { return new Pagination(); }

        public Pagination page(int page) { this.page = page; return this; }
        public Pagination size(int size) { this.size = size; return this; }
        public Pagination total(int total) { this.total = total; return this; }

        public Pagination build() { return this; }

        // Getters
        public int getPage() { return page; }
        public int getSize() { return size; }
        public int getTotal() { return total; }
    }
}
