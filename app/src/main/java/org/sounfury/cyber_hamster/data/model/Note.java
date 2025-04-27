package org.sounfury.cyber_hamster.data.model;

import java.util.Date;

public class Note {
    private int id;
    private int bookId;
    private String title;
    private String content;
    private Date createTime;
    private Date updateTime;
    
    // 构造方法、getter和setter方法
    
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
} 