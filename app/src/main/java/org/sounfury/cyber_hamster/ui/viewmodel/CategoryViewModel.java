package org.sounfury.cyber_hamster.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.sounfury.cyber_hamster.data.model.Category;
import org.sounfury.cyber_hamster.data.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CategoryViewModel extends ViewModel {
    
    private final CategoryRepository categoryRepository;
    private final MutableLiveData<List<Category>> categoryList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> operationSuccess = new MutableLiveData<>();
    
    private final CompositeDisposable disposables = new CompositeDisposable();
    
    public CategoryViewModel() {
        this.categoryRepository = CategoryRepository.getInstance();
        loadCategories();
    }
    
    public LiveData<List<Category>> getCategoryList() {
        return categoryList;
    }
    
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public LiveData<Boolean> getOperationSuccess() {
        return operationSuccess;
    }
    
    public void loadCategories() {
        isLoading.setValue(true);
        
        Disposable disposable = categoryRepository.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categories -> {
                    categoryList.setValue(categories);
                    isLoading.setValue(false);
                }, throwable -> {
                    Log.e("CategoryViewModel", "Error loading categories", throwable);
                    errorMessage.setValue("获取书架列表失败：" + throwable.getMessage());
                    isLoading.setValue(false);
                });
        
        disposables.add(disposable);
    }
    
    public void createCategory(String name) {
        isLoading.setValue(true);
        
        Disposable disposable = categoryRepository.createCategory(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(category -> {
                    if (category != null) {
                        operationSuccess.setValue(true);
                        loadCategories(); // 重新加载列表
                    } else {
                        errorMessage.setValue("创建书架失败");
                    }
                    isLoading.setValue(false);
                }, throwable -> {
                    Log.e("CategoryViewModel", "Error creating category", throwable);
                    errorMessage.setValue("创建书架失败：" + throwable.getMessage());
                    isLoading.setValue(false);
                });
        
        disposables.add(disposable);
    }
    
    public void updateCategory(Category category) {
        isLoading.setValue(true);
        
        Disposable disposable = categoryRepository.updateCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updatedCategory -> {
                    if (updatedCategory != null) {
                        operationSuccess.setValue(true);
                        loadCategories(); // 重新加载列表
                    } else {
                        errorMessage.setValue("更新书架失败");
                    }
                    isLoading.setValue(false);
                }, throwable -> {
                    Log.e("CategoryViewModel", "Error updating category", throwable);
                    errorMessage.setValue("更新书架失败：" + throwable.getMessage());
                    isLoading.setValue(false);
                });
        
        disposables.add(disposable);
    }
    
    public void deleteCategory(long categoryId) {
        isLoading.setValue(true);
        
        Disposable disposable = categoryRepository.deleteCategory(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                    if (success) {
                        operationSuccess.setValue(true);
                        loadCategories(); // 重新加载列表
                    } else {
                        errorMessage.setValue("删除书架失败");
                    }
                    isLoading.setValue(false);
                }, throwable -> {
                    Log.e("CategoryViewModel", "Error deleting category", throwable);
                    errorMessage.setValue("删除书架失败：" + throwable.getMessage());
                    isLoading.setValue(false);
                });
        
        disposables.add(disposable);
    }
    
//    public void addBooksToCategory(long categoryId, List<Long> bookIds) {
//        isLoading.setValue(true);
//
//        Disposable disposable = categoryRepository.addBooksToCategory(categoryId, bookIds)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(success -> {
//                    if (success) {
//                        operationSuccess.setValue(true);
//                    } else {
//                        errorMessage.setValue("添加图书到书架失败");
//                    }
//                    isLoading.setValue(false);
//                }, throwable -> {
//                    Log.e("CategoryViewModel", "Error adding books to category", throwable);
//                    errorMessage.setValue("添加图书到书架失败：" + throwable.getMessage());
//                    isLoading.setValue(false);
//                });
//
//        disposables.add(disposable);
//    }
    

    
    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
} 