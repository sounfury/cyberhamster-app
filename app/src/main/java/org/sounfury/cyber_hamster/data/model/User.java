package org.sounfury.cyber_hamster.data.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    
    // 构造方法、getter和setter方法
    
    public User() {
    }
    
    public User(int id, String username, String password, String nickname, String avatar) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.avatar = avatar;
    }
    
    // Getter和Setter方法
} 