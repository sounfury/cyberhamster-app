package org.sounfury.cyber_hamster.data.model;

import java.util.Date;

public class Note {
    private int id;
    private int bookId;
    private String title;
    private String content;
    private Date createTime;
    private Date updateTime;
    
    public Note() {
    }
    
    public Note(int id, int bookId, String title, String content, Date createTime, Date updateTime) {
        this.id = id;
        this.bookId = bookId;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
    
    // Getter和Setter方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
} 