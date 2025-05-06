package org.sounfury.cyber_hamster.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.jeremyliao.liveeventbus.LiveEventBus;

import org.sounfury.cyber_hamster.data.model.Book;
import org.sounfury.cyber_hamster.data.model.Category;
import org.sounfury.cyber_hamster.data.repository.BookRepository;
import org.sounfury.cyber_hamster.data.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {
    
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final MutableLiveData<List<Book>> bookList = new MutableLiveData<>();
    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectedCategory = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    private final CompositeDisposable disposables = new CompositeDisposable();
    
    // 保存观察者引用以便在onCleared()中移除
    private final Observer<Boolean> bookAddedObserver = isSuccess -> {
        if (isSuccess) {
            // 图书入库成功，刷新数据
            refreshData();
        }
    };
    
    private int currentPage = 0;
    private boolean hasMoreData = true;
    private List<Book> currentBooks = new ArrayList<>();
    
    public HomeViewModel() {
        this.bookRepository = BookRepository.getInstance();
        this.categoryRepository = CategoryRepository.getInstance();
        loadCategories();
        loadHomeBooks();
        selectedCategory.setValue(0); // 默认选中全部分类
        
        // 订阅图书入库成功事件
        LiveEventBus.get(AddBookViewModel.EVENT_BOOK_ADDED, Boolean.class)
                .observeForever(bookAddedObserver);
    }
    
    public LiveData<List<Book>> getBookList() {
        return bookList;
    }
    
    public LiveData<List<Category>> getCategories() {
        return categories;
    }
    
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public LiveData<Integer> getSelectedCategory() {
        return selectedCategory;
    }
    
    public void setSelectedCategory(int categoryId) {
        selectedCategory.setValue(categoryId);
        // 切换分类时重置分页
        resetPagination();
        filterBooksByCategory(categoryId);
    }

    private void resetPagination() {
        currentPage = 0;
        hasMoreData = true;
        currentBooks.clear();
    }
    
    public void loadCategories() {
        isLoading.setValue(true);
        
        Disposable disposable = categoryRepository.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categoryList -> {
                    categories.setValue(categoryList);
                    isLoading.setValue(false);
                }, throwable -> {
                    Log.e("HomeViewModel", "Error loading categories", throwable);
                    errorMessage.setValue("获取分类失败：" + throwable.getMessage());
                    isLoading.setValue(false);
                });
        
        disposables.add(disposable);
    }

    public void loadHomeBooks() {
        if (!hasMoreData) {
            return;
        }
        
        isLoading.setValue(true);

        Disposable disposable = bookRepository.getAllBooks(currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(books -> {
                    // 判断是否还有更多数据
                    if (books.isEmpty()) {
                        hasMoreData = false;
                    } else {
                        // 添加新数据到列表
                        if (currentPage == 0) {
                            currentBooks.clear();
                        }
                        currentBooks.addAll(books);
                        bookList.setValue(currentBooks);
                    }
                    isLoading.setValue(false);
                }, throwable -> {
                    Log.e("HomeViewModel", "Error loading books", throwable);
                    errorMessage.setValue("获取图书失败：" + throwable.getMessage());
                    isLoading.setValue(false);
                });

        disposables.add(disposable);
    }

    public void loadNextPage() {
        if (!isLoading.getValue() && hasMoreData) {
            currentPage++;
            
            // 如果当前选中的是全部分类，加载所有图书；否则按分类加载
            Integer categoryId = selectedCategory.getValue();
            if (categoryId != null && categoryId > 0) {
                loadBooksByCategory(categoryId);
            } else {
                loadHomeBooks();
            }
        }
    }

    // ViewModel销毁时取消订阅，防止内存泄漏
    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
        
        // 移除LiveEventBus观察者以防止内存泄漏
        LiveEventBus.get(AddBookViewModel.EVENT_BOOK_ADDED, Boolean.class)
                .removeObserver(bookAddedObserver);
    }

    private void filterBooksByCategory(int categoryId) {
        isLoading.setValue(true);
        
        if (categoryId == 0) {
            // 如果是"全部"分类，加载所有图书
            loadHomeBooks();
        } else {
            // 按照选中的分类加载图书
            loadBooksByCategory(categoryId);
        }
    }
    
    private void loadBooksByCategory(int categoryId) {
        isLoading.setValue(true);
        
        Disposable disposable = categoryRepository.getBooksByCategoryId(categoryId, currentPage, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pageResult -> {
                    List<Book> books = pageResult.getRows();
                    
                    // 判断是否还有更多数据
                    if (books == null || books.isEmpty() || books.size() < 10) {
                        hasMoreData = false;
                    }
                    
                    // 更新UI
                    if (currentPage == 0) {
                        currentBooks.clear();
                    }
                    
                    if (books != null && !books.isEmpty()) {
                        currentBooks.addAll(books);
                    }
                    
                    bookList.setValue(currentBooks);
                    isLoading.setValue(false);
                }, throwable -> {
                    Log.e("HomeViewModel", "Error loading books by category", throwable);
                    errorMessage.setValue("获取分类图书失败：" + throwable.getMessage());
                    isLoading.setValue(false);
                });
        
        disposables.add(disposable);
    }

    /**
     * 刷新数据
     * 重置分页并重新加载数据
     */
    public void refreshData() {
        // 重置分页状态
        resetPagination();
        
        // 根据当前选中的分类重新加载数据
        Integer categoryId = selectedCategory.getValue();
        if (categoryId != null && categoryId > 0) {
            loadBooksByCategory(categoryId);
        } else {
            loadHomeBooks();
        }
    }
} 