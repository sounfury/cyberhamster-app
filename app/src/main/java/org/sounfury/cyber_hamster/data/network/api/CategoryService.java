package org.sounfury.cyber_hamster.data.network.api;

import org.sounfury.cyber_hamster.data.model.Book;
import org.sounfury.cyber_hamster.data.model.Category;
import org.sounfury.cyber_hamster.data.network.page.PageQuery;
import org.sounfury.cyber_hamster.data.network.page.PageResult;
import org.sounfury.cyber_hamster.data.network.request.AddBookToCategoryRequest;
import org.sounfury.cyber_hamster.data.network.request.AddBooksToCategoryRequest;
import org.sounfury.cyber_hamster.data.network.response.Result;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CategoryService {

    /**
     * 获取书籍分类
     */
    @GET("/api/book-category/list")
    Observable<Result<List<Category>>> getBookCategories();

    /**
     * 分页获取某个分类的书籍列表
     */
    @POST("/api/book-category/{categoryId}/books")
    Observable<Result<PageResult<Book>>> getBooksByCategoryId(@Path("categoryId") long categoryId, @Body PageQuery<Book> pageQuery);

    /**
     * 创建分类
     */
    @POST("/api/book-category")
    Observable<Result<Void>> createCategory(@Query("categoryName") String name);

    /**
     * 更新分类
     */
    @PUT("/api/book-category")
    Observable<Result<Category>> updateCategory(@Body Category category);


    /**
     * 删除分类
     */
    @DELETE("/api/book-category/{categoryId}")
    Observable<Result<Void>> deleteCategory(@Path("categoryId") long categoryId);

    /**
     * 批量将图书替换到分类
     */
    @POST("/api/book-category/books-add")
    Observable<Result<Void>> addBooksToCategory(@Body AddBooksToCategoryRequest addBooksToCategoryRequest);



    /**
     * 单独将图书添加或删除到多个分类
     */
    @POST("/api/book-category/book-add")
    Observable<Result<Void>> addBookToCategory(@Body AddBookToCategoryRequest addBooksToCategoryRequest);




}
