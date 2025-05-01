package org.sounfury.cyber_hamster.data.network.response;

import org.sounfury.cyber_hamster.data.model.Category;

import java.util.List;
public class GroupedBooks {
    private int id;
    private String bookName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public List<Category> getUserCategories() {
        return userCategories;
    }

    public GroupedBooks(int id, String bookName, String author, List<Category> userCategories, String press) {
        this.id = id;
        this.bookName = bookName;
        this.author = author;
        this.userCategories = userCategories;
        this.press = press;
    }

    public void setUserCategories(List<Category> userCategories) {
        this.userCategories = userCategories;
    }

    private String author;
    private String press;
    private List<Category> userCategories;
}
