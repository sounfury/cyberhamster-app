package org.sounfury.cyber_hamster.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.jeremyliao.liveeventbus.LiveEventBus;

import org.sounfury.cyber_hamster.data.network.response.GroupedBooks;
import org.sounfury.cyber_hamster.data.repository.BookRepository;
import org.sounfury.cyber_hamster.data.repository.CategoryRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BookshelfViewModel extends ViewModel {
    
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final MutableLiveData<List<GroupedBooks>> bookList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> operationSuccess = new MutableLiveData<>();
    
    private final CompositeDisposable disposables = new CompositeDisposable();
    
    // 保存观察者引用以便在onCleared()中移除
    private final Observer<Boolean> bookAddedObserver = isSuccess -> {
        if (isSuccess) {
            // 图书入库成功，刷新数据
            // 重置页码并重新加载数据
            currentPage = 0;
            loadBooksToGrouped();
        }
    };
    
    private int currentPage = 0;
    private boolean hasMoreData = true;
    
    public BookshelfViewModel() {
        this.bookRepository = BookRepository.getInstance();
        this.categoryRepository = CategoryRepository.getInstance();
        
        // 订阅图书入库成功事件
        LiveEventBus.get(AddBookViewModel.EVENT_BOOK_ADDED, Boolean.class)
                .observeForever(bookAddedObserver);
    }
    
    public LiveData<List<GroupedBooks>> getBookList() {
        return bookList;
    }
    
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public LiveData<String> getOperationSuccess() {
        return operationSuccess;
    }
    
    public void loadBooksToGrouped() {
        isLoading.setValue(true);
        
        Disposable disposable = bookRepository.getBooksToGroup(currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(books -> {
                    bookList.setValue(books);
                    isLoading.setValue(false);
                }, throwable -> {
                    Log.e("BookshelfViewModel", "Error loading books", throwable);
                    errorMessage.setValue("获取图书列表失败：" + throwable.getMessage());
                    isLoading.setValue(false);
                });
        
        disposables.add(disposable);
    }
    
    public void loadNextPage() {
        if (isLoading.getValue() != null && isLoading.getValue() || !hasMoreData) {
            return;
        }
        currentPage++;
        loadBooksToGrouped();
    }
    
    // 将书籍添加到分类
    public void addBooksToCategories(List<Long> bookIds, List<Long> categoryIds) {
        isLoading.setValue(true);
        
        Disposable disposable = categoryRepository.addBooksToCategories(categoryIds, bookIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                    if (success) {
                        if (categoryIds.isEmpty()) {
                            operationSuccess.setValue("成功解除所有分类");
                        } else {
                            operationSuccess.setValue("成功添加到书架");
                        }
                        loadBooksToGrouped(); // 刷新列表
                    } else {
                        errorMessage.setValue("操作失败");
                    }
                    isLoading.setValue(false);
                }, throwable -> {
                    Log.e("BookshelfViewModel", "Error adding books to categories", throwable);
                    errorMessage.setValue("操作失败：" + throwable.getMessage());
                    isLoading.setValue(false);
                });
        
        disposables.add(disposable);
    }
    
    // 添加单本图书到多个分类
    public void addSingleBookToCategories(long bookId, List<Long> categoryIds) {
        isLoading.setValue(true);
        
        Disposable disposable = categoryRepository.addSingleBookToCategories(bookId, categoryIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                    if (success) {
                        if (categoryIds.isEmpty()) {
                            operationSuccess.setValue("成功解除所有分类");
                        } else {
                            operationSuccess.setValue("成功添加到书架");
                        }
                        loadBooksToGrouped(); // 刷新列表
                    } else {
                        errorMessage.setValue("操作失败");
                    }
                    isLoading.setValue(false);
                }, throwable -> {
                    Log.e("BookshelfViewModel", "Error adding single book to categories", throwable);
                    errorMessage.setValue("操作失败：" + throwable.getMessage());
                    isLoading.setValue(false);
                });
        
        disposables.add(disposable);
    }
    
    // 删除书籍
    public void deleteBooks(List<Long> bookIds) {
        isLoading.setValue(true);
        
        // TODO: 实现删除书籍的API调用
        // 模拟成功
        operationSuccess.setValue("删除成功");
        isLoading.setValue(false);
        
        // 刷新列表
        currentPage = 0;
        loadBooksToGrouped();
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
        
        // 移除LiveEventBus观察者以防止内存泄漏
        LiveEventBus.get(AddBookViewModel.EVENT_BOOK_ADDED, Boolean.class)
                .removeObserver(bookAddedObserver);
    }
} 