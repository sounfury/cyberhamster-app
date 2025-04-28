package org.sounfury.cyber_hamster.config;

public class Constants {
    
    // API相关常量
    public static final String BASE_URL = "https://127.0.0.1:8081/";
    
    // SharedPreferences相关常量
    public static final String SHARED_PREFS_NAME = "cyber_hamster_prefs";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_TOKEN = "token";
    
    // 图书阅读状态
    public static final int READ_STATUS_UNREAD = 0;
    public static final int READ_STATUS_READING = 1;
    public static final int READ_STATUS_FINISHED = 2;
    
    // Intent传递参数的Key
    public static final String KEY_BOOK_ID = "book_id";
    public static final String KEY_NOTE_ID = "note_id";
    public static final String KEY_CATEGORY_ID = "category_id";
} 