package org.sounfury.cyber_hamster.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.sounfury.cyber_hamster.data.model.Book;
import org.sounfury.cyber_hamster.data.repository.BookRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeViewModel extends ViewModel {
    
    private final BookRepository bookRepository;
    private final MutableLiveData<List<Book>> bookList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectedCategory = new MutableLiveData<>();
    
    public HomeViewModel() {
        // 在实际项目中，这应该通过依赖注入提供
        this.bookRepository = new BookRepository();
        loadBooks();
        selectedCategory.setValue(0); // 默认选中全部分类
    }
    
    public LiveData<List<Book>> getBookList() {
        return bookList;
    }
    
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<Integer> getSelectedCategory() {
        return selectedCategory;
    }
    
    public void setSelectedCategory(int categoryId) {
        selectedCategory.setValue(categoryId);
        filterBooksByCategory(categoryId);
    }
    
    public void loadBooks() {
        isLoading.setValue(true);
        
        // 在实际应用中，这里应该调用Repository中的异步方法获取数据
        // 此处为简化，直接使用模拟数据
        List<Book> books = loadMockData();
        bookList.setValue(books);
        
        isLoading.setValue(false);
    }
    
    private void filterBooksByCategory(int categoryId) {
        isLoading.setValue(true);
        
        if (categoryId == 0) {
            // 如果是"全部"分类，加载所有图书
            loadBooks();
        } else {
            // 否则，根据分类ID筛选
            List<Book> allBooks = loadMockData();
            List<Book> filteredBooks = new ArrayList<>();
            
            for (Book book : allBooks) {
                if (book.getCategoryId() == categoryId) {
                    filteredBooks.add(book);
                }
            }
            
            bookList.setValue(filteredBooks);
        }
        
        isLoading.setValue(false);
    }
    
    // 加载模拟数据
    private List<Book> loadMockData() {
//        // 添加一些模拟数据
        // 创建一个 List<Book> 用于存放书籍对象
        List<Book> books = new ArrayList<>();

// 往 books 中添加一些模拟数据
        books.add(new Book(
                1L,
                "TP312",
                "978-7-111-61586-3",
                "深入理解Java虚拟机",
                "周志明",
                "https://example.com/jvm.jpg",
                "机械工业出版社",
                "2019-03",
                "北京",
                "计算机科学",
                "讲解了Java虚拟机的原理、运行机制及优化方法",
                "平装",
                "中文",
                "16开",
                400,
                "第3版",
                "500千字",
                1001L
        ));

        books.add(new Book(
                2L,
                "TP312",
                "978-7-302-58047-2",
                "计算机网络：自顶向下方法",
                "James F. Kurose",
                "https://example.com/network.jpg",
                "清华大学出版社",
                "2021-07",
                "北京",
                "计算机科学",
                "以应用层到物理层的顺序讲述计算机网络",
                "精装",
                "中文",
                "16开",
                600,
                "第7版",
                "700千字",
                1002L
        ));

        books.add(new Book(
                3L,
                "TP311",
                "978-7-115-50938-1",
                "算法（第4版）",
                "Robert Sedgewick",
                "https://example.com/algorithm.jpg",
                "人民邮电出版社",
                "2018-01",
                "北京",
                "计算机科学",
                "经典算法教材，涵盖排序、查找、图、字符串处理等",
                "平装",
                "中文",
                "16开",
                920,
                "第4版",
                "1000千字",
                1003L
        ));


        return books;
    }
} 