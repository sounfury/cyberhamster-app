package org.sounfury.cyber_hamster.data.model;

import java.util.Date;

public class Book {

    /**
     * 图书ID
     */
    private long id;

//    private UserBook userBook;

    public Book(long id, String clcCode, String isbn, String bookName, String author, String coverUrl, String press, String pressDate, String pressPlace, String clcName, String bookDesc, String binding, String language, String format, Integer pages, String edition, String words, Long categoryId) {
        this.id = id;
        this.clcCode = clcCode;
        this.isbn = isbn;
        this.bookName = bookName;
        this.author = author;
        this.coverUrl = coverUrl;
        this.press = press;
        this.pressDate = pressDate;
        this.pressPlace = pressPlace;
        this.clcName = clcName;
        this.bookDesc = bookDesc;
        this.binding = binding;
        this.language = language;
        this.format = format;
        this.pages = pages;
        this.edition = edition;
        this.words = words;
        this.categoryId = categoryId;
    }

    /**
     * ISBN号，唯一约束
     */
    private String isbn;

    /**
     * 书名
     */
    private String bookName;

    /**
     * 作者
     */
    private String author;

    /**
     * 封面URL
     */
    private String coverUrl;

    public Book() {

    }

    public String[] getCoverUrlArray() {
        if (coverUrl == null) {
            return new String[0];
        }
        try {
            // 使用Gson将字符串再解析成数组
            return new com.google.gson.Gson().fromJson(coverUrl, String[].class);
        } catch (Exception e) {
            e.printStackTrace();
            return new String[0];
        }
    }



    /**
     * 出版社
     */
    private String press;

    /**
     * 出版日期
     */
    private String pressDate;

    /**
     * 出版地
     */
    private String pressPlace;

    /**
     * 中图法分类
     */
    private String clcCode;

    /**
     * 中图法分类名
     */
    private String clcName;

    /**
     * 简介
     */
    private String bookDesc;

    /**
     * 装订方式
     */
    private String binding;

    /**
     * 语言
     */
    private String language;

    /**
     * 开本
     */
    private String format;

    /**
     * 页数
     */
    private Integer pages;

    /**
     * 版次
     */
    private String edition;

    /**
     * 字数
     */
    private String words;


    private Long categoryId;



    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    // Getter 和 Setter 方法
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getPressDate() {
        return pressDate;
    }

    public void setPressDate(String pressDate) {
        this.pressDate = pressDate;
    }

    public String getPressPlace() {
        return pressPlace;
    }

    public void setPressPlace(String pressPlace) {
        this.pressPlace = pressPlace;
    }

    public String getClcCode() {
        return clcCode;
    }

    public void setClcCode(String clcCode) {
        this.clcCode = clcCode;
    }

    public String getClcName() {
        return clcName;
    }

    public void setClcName(String clcName) {
        this.clcName = clcName;
    }

    public String getBookDesc() {
        return bookDesc;
    }

    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }



    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }








}
