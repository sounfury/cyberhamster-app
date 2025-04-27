package org.sounfury.cyber_hamster.data.network;

import java.util.List;

import org.sounfury.cyber_hamster.data.model.Book;
import org.sounfury.cyber_hamster.data.model.Category;
import org.sounfury.cyber_hamster.data.model.Note;
import org.sounfury.cyber_hamster.data.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    
    // 用户相关接口
    @POST("login")
    Call<User> login(@Body User user);
    
    @POST("register")
    Call<User> register(@Body User user);
    
    // 图书相关接口
    @GET("books")
    Call<List<Book>> getBooks();
    
    @GET("books/{id}")
    Call<Book> getBookById(@Path("id") int id);
    
    @POST("books")
    Call<Book> addBook(@Body Book book);
    
    @PUT("books/{id}")
    Call<Book> updateBook(@Path("id") int id, @Body Book book);
    
    @DELETE("books/{id}")
    Call<Void> deleteBook(@Path("id") int id);
    
    // 笔记相关接口
    @GET("notes")
    Call<List<Note>> getNotes(@Query("bookId") int bookId);
    
    @GET("notes/{id}")
    Call<Note> getNoteById(@Path("id") int id);
    
    @POST("notes")
    Call<Note> addNote(@Body Note note);
    
    @PUT("notes/{id}")
    Call<Note> updateNote(@Path("id") int id, @Body Note note);
    
    @DELETE("notes/{id}")
    Call<Void> deleteNote(@Path("id") int id);
    
    // 分类相关接口
    @GET("categories")
    Call<List<Category>> getCategories();
} 