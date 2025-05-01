package org.sounfury.cyber_hamster.data.network.request;

import java.util.List;

public class AddBookToCategoryRequest {
    private List<Long> categoryIds;

    public AddBookToCategoryRequest(List<Long> categoryIds, long bookId) {
        this.categoryIds = categoryIds;
        this.bookId = bookId;
    }

    private long bookId;

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }
}
