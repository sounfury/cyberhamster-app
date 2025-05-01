package org.sounfury.cyber_hamster.data.network.request;

import java.util.List;

public class AddBooksToCategoryRequest {
    private List<Long> categoryIds;
    private List<Long> bookIds;

    // Getter 和 Setter
    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<Long> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Long> bookIds) {
        this.bookIds = bookIds;
    }
}