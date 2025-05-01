package org.sounfury.cyber_hamster.data.model;

import java.util.List;

public class Category {
    private int id;

    public List<Long> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Long> bookIds) {
        this.bookIds = bookIds;
    }

    private String name;
    private List<Long> bookIds;
    
    // 构造方法、getter和setter方法
    
    public Category() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Category(int id, String name, String description) {
        this.id = id;
        this.name = name;
    }
    
    // Getter和Setter方法
} 