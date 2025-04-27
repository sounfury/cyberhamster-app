package org.sounfury.cyber_hamster.data.model;

import java.util.Date;

public class Book {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private String coverUrl;
    private String description;
    private int categoryId;
    private Date addDate;
    private int readStatus; // 0-未读, 1-在读, 2-已读
    
    // 构造方法、getter和setter方法
    
    public Book() {
    }
    
    public Book(int id, String title, String author, String publisher, String isbn, String coverUrl, String description, int categoryId, Date addDate, int readStatus) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.coverUrl = coverUrl;
        this.description = description;
        this.categoryId = categoryId;
        this.addDate = addDate;
        this.readStatus = readStatus;
    }
    
    // Getter和Setter方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }
} 