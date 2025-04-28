package org.sounfury.cyber_hamster.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.sounfury.cyber_hamster.data.model.Category;
import org.sounfury.cyber_hamster.data.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewModel extends ViewModel {
    
    private final CategoryRepository categoryRepository;
    private final MutableLiveData<List<Category>> categoryList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    
    public CategoryViewModel() {
        // 在实际项目中，这应该通过依赖注入提供
        this.categoryRepository = new CategoryRepository();
        loadCategories();
    }
    
    public LiveData<List<Category>> getCategoryList() {
        return categoryList;
    }
    
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public void loadCategories() {
        isLoading.setValue(true);
        
        // 在实际应用中，这里应该调用Repository中的异步方法获取数据
        // 此处为简化，直接使用模拟数据
        List<Category> categories = loadMockData();
        categoryList.setValue(categories);
        
        isLoading.setValue(false);
    }
    
    // 加载模拟数据
    private List<Category> loadMockData() {
        List<Category> categories = new ArrayList<>();
        
        // 添加一些模拟数据
        categories.add(new Category(1, "小说", "文学小说类"));
        categories.add(new Category(2, "科技", "科学技术类"));
        categories.add(new Category(3, "社科", "社会科学类"));
        categories.add(new Category(4, "教育", "教育相关类"));
        categories.add(new Category(5, "经济", "经济管理类"));
        
        return categories;
    }
} 