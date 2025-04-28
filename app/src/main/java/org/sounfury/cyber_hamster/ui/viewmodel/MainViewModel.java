package org.sounfury.cyber_hamster.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    
    private final MutableLiveData<Integer> currentPage = new MutableLiveData<>();
    
    public MainViewModel() {
        // 默认页面是首页
        currentPage.setValue(0);
    }
    
    public LiveData<Integer> getCurrentPage() {
        return currentPage;
    }
    
    public void setCurrentPage(int page) {
        currentPage.setValue(page);
    }
} 