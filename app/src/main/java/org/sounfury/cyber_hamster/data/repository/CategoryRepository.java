package org.sounfury.cyber_hamster.data.repository;

import org.sounfury.cyber_hamster.data.model.Book;
import org.sounfury.cyber_hamster.data.model.Category;
import org.sounfury.cyber_hamster.data.network.RetrofitClient;
import org.sounfury.cyber_hamster.data.network.api.CategoryService;
import org.sounfury.cyber_hamster.data.network.page.PageQuery;
import org.sounfury.cyber_hamster.data.network.page.PageResult;
import org.sounfury.cyber_hamster.data.network.request.AddBookToCategoryRequest;
import org.sounfury.cyber_hamster.data.network.request.AddBooksToCategoryRequest;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CategoryRepository {
    
    private static CategoryRepository instance = new CategoryRepository();
    private final CategoryService categoryService;
    
    private CategoryRepository() {
        this.categoryService = RetrofitClient.getInstance().getCategoryService();
    }
    
    public static CategoryRepository getInstance() {
        return instance;
    }
    
    // 从API获取所有分类
    public Observable<List<Category>> getAllCategories() {
        return categoryService.getBookCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(result -> {
                    if (result != null && result.isSuccess() && result.getData() != null) {
                        return result.getData();
                    } else {
                        return new ArrayList<>();
                    }
                });
    }
    
    // 根据分类ID分页获取图书
    public Observable<PageResult<Book>> getBooksByCategoryId(long categoryId, int pageNum, int pageSize) {
        PageQuery<Book> pageQuery = new PageQuery<>(pageNum, pageSize);
        return categoryService.getBooksByCategoryId(categoryId, pageQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(result -> {
                    if (result != null && result.isSuccess() && result.getData() != null) {
                        return result.getData();
                    } else {
                        return new PageResult<>();
                    }
                });
    }

    // 创建新分类
    public Observable<Boolean> createCategory(String name) {
        return categoryService.createCategory(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(result -> result != null && result.isSuccess());
    }
    
    // 更新分类
    public Observable<Category> updateCategory(Category category) {
        return categoryService.updateCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(result -> {
                    if (result != null && result.isSuccess() && result.getData() != null) {
                        return result.getData();
                    } else {
                        return null;
                    }
                });
    }
    
    // 删除分类
    public Observable<Boolean> deleteCategory(long categoryId) {
        return categoryService.deleteCategory(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(result -> result != null && result.isSuccess());
    }
    
//    // 将图书添加到分类
//    public Observable<Boolean> addBooksToCategory(long categoryId, List<Long> bookIds) {
//        return categoryService.addBooksToCategory(categoryId, bookIds)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .map(result -> result != null && result.isSuccess());
//    }
    
    // 批量将图书添加到多个分类（或从所有分类中移除）
    public Observable<Boolean> addBooksToCategories(List<Long> categoryIds, List<Long> bookIds) {
        AddBooksToCategoryRequest request = new AddBooksToCategoryRequest();
        request.setCategoryIds(categoryIds);
        request.setBookIds(bookIds);
        
        return categoryService.addBooksToCategory(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(result -> result != null && result.isSuccess());
    }
    
    // 将单本图书添加到多个分类（或从所有分类中移除）
    public Observable<Boolean> addSingleBookToCategories(long bookId, List<Long> categoryIds) {
        AddBookToCategoryRequest request = new AddBookToCategoryRequest(categoryIds, bookId);
        
        return categoryService.addBookToCategory(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(result -> result != null && result.isSuccess());
    }
} 