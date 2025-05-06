package org.sounfury.cyber_hamster.data.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.sounfury.cyber_hamster.data.model.Book;
import org.sounfury.cyber_hamster.data.network.response.Result;
import org.sounfury.cyber_hamster.data.model.UserBook;
import org.sounfury.cyber_hamster.data.network.RetrofitClient;
import org.sounfury.cyber_hamster.data.network.api.BookService;
import org.sounfury.cyber_hamster.data.network.page.PageQuery;
import org.sounfury.cyber_hamster.data.network.request.BookListQuery;
import org.sounfury.cyber_hamster.data.network.response.GroupedBooks;
import org.sounfury.cyber_hamster.data.network.page.PageResult;
import org.sounfury.cyber_hamster.data.network.request.InboundInput;
import org.sounfury.cyber_hamster.data.network.request.ReadStatusUpdateDTO;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BookRepository {

    private static BookRepository instance;
    private BookService bookService;

    private int pageSize = 10;

    private BookRepository() {
        bookService = RetrofitClient.getInstance().getBookService();
    }

    public static synchronized BookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }

    // 获取所有图书
    public Observable<List<Book>> getAllBooks(int currentPage) {
        // 创建分页查询对象
        PageQuery<Book> pageQuery = new PageQuery<>(currentPage, pageSize);

        // 调用API接口，异步处理
        return bookService.listBooks(pageQuery)
                .subscribeOn(Schedulers.io()) // 指定在IO线程发起请求
                .observeOn(AndroidSchedulers.mainThread()) // 指定在主线程回调，如果是Android的话
                .map(apiResponse -> {
                    // 转换响应结果
                    if (apiResponse != null && apiResponse.isSuccess() && apiResponse.getData() != null) {
                        return apiResponse.getData().getRows();
                    } else {
                        return new ArrayList<>(); // 出错返回空列表，防止空指针
                    }
                });
    }
    
    // 获取获取待变更分组的图书列表
    public Observable<List<GroupedBooks>> getBooksToGroup(int currentPage) {
        // 创建查询对象
        PageQuery<GroupedBooks> pageQuery = new PageQuery<>(currentPage, pageSize);
        
        // 调用API接口
        return bookService.listBooksToGrouped(pageQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(apiResponse -> {
                    if (apiResponse != null && apiResponse.isSuccess() && apiResponse.getData() != null) {
                        return apiResponse.getData().getRows();
                    } else {
                        return new ArrayList<>();
                    }
                });
    }
    
    // 根据ID获取图书详情
    public Observable<UserBook> getBookById(long id) {
        return bookService.getBookById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(apiResponse -> {
                    if (apiResponse != null && apiResponse.isSuccess() && apiResponse.getData() != null) {
                        return apiResponse.getData();
                    } else {
                        throw new RuntimeException("获取图书详情失败");
                    }
                });
    }
    
    // 添加图书
    public boolean addBook(Book book) {
        // 实现添加图书的逻辑
        return false;
    }
    
    // 更新图书
    public boolean updateBook(Book book) {
        // 实现更新图书的逻辑
        return false;
    }
    
    // 删除图书
    public boolean deleteBook(int id) {
        // 实现删除图书的逻辑
        return false;
    }
    
    // 根据分类获取图书
    public List<Book> getBooksByCategory(int categoryId) {
        // 实现根据分类获取图书的逻辑
        return null;
    }


    
    /**
     * 图书入库
     * @param inboundInput 入库请求体
     */
    public Observable<Result<Void>> inboundBook(InboundInput inboundInput) {
        return bookService.inbound(inboundInput);
    }
    
    /**
     * 更新图书阅读状态
     * @param readStatusUpdateDTO 阅读状态更新请求体
     * @return 更新结果
     */
    public Observable<Result<Void>> updateReadStatus(ReadStatusUpdateDTO readStatusUpdateDTO) {
        return bookService.updateReadStatus(readStatusUpdateDTO);
    }
} 