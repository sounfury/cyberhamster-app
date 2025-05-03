package org.sounfury.cyber_hamster.data.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.Expose;

import java.util.Date;

public class Note {
    private Long id;
    
    @SerializedName("bookId")
    private long bookId;
    
    private String title;
    private String content;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @SerializedName("createTime")
    private String createTime;
    
    @SerializedName("updateTime")
    private String updateTime;

    /**
     * 笔记摘要
     */
    private String summary;


    private Book book;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Note() {
    }
    


    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Note(Long id, long bookId, String title, String content, String createTime, String updateTime, String summary) {
        this.id = id;
        this.bookId = bookId;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date date) {
        // 将Date转换为字符串格式 yyyy-MM-dd'T'HH:mm:ss
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        this.createTime = sdf.format(date);
    }
    
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date date) {
        // 将Date转换为字符串格式 yyyy-MM-dd'T'HH:mm:ss
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        this.updateTime = sdf.format(date);
    }
    
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
} 