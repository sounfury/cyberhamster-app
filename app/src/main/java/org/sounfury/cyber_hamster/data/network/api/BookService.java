package org.sounfury.cyber_hamster.data.network.api;

import org.sounfury.cyber_hamster.data.model.Book;
import org.sounfury.cyber_hamster.data.model.UserBook;
import org.sounfury.cyber_hamster.data.network.page.PageQuery;
import org.sounfury.cyber_hamster.data.network.page.PageResult;
import org.sounfury.cyber_hamster.data.network.request.BookListQuery;
import org.sounfury.cyber_hamster.data.network.request.InboundInput;
import org.sounfury.cyber_hamster.data.network.response.GroupedBooks;
import org.sounfury.cyber_hamster.data.network.response.Result;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookService {

    //入库 /api/inbound
    @POST("/api/inbound/isbn")
    Observable<Result<Void>> inbound(@Body InboundInput inboundInput);


    //分页 /api/user-book/list 传入PageQuery
    @POST("/api/user-book/list")
    Observable<Result<PageResult<Book>>> listBooks(@Body PageQuery<Book> pageQuery);

    /**
     * 获取图书详情，只在首页点进去书籍列表中使用
     * @param id
     * @return
     */
    @GET("/api/user-book/{id}")
    Observable<Result<UserBook>> getBookById(@Path("id") long id);

    //分页 待分组的图书，在书架管理里用
    @POST("/api/user-book/list-toGrouped")
    Observable<Result<PageResult<GroupedBooks>>> listBooksToGrouped(@Body PageQuery<GroupedBooks> pageQuery);

}
