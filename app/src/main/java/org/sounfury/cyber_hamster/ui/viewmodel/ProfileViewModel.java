package org.sounfury.cyber_hamster.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.sounfury.cyber_hamster.data.model.User;

public class ProfileViewModel extends ViewModel {
    
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();
    private final MutableLiveData<Integer> bookCount = new MutableLiveData<>();
    private final MutableLiveData<Integer> noteCount = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    
    public ProfileViewModel() {
        loadUserProfile();
    }
    
    public LiveData<User> getCurrentUser() {
        return currentUser;
    }
    
    public LiveData<Integer> getBookCount() {
        return bookCount;
    }
    
    public LiveData<Integer> getNoteCount() {
        return noteCount;
    }
    
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public void loadUserProfile() {
        isLoading.setValue(true);
        
        // 在实际应用中，这里应该从Repository中获取用户数据
        // 此处为模拟数据
        User user = new User(1, "user123", "password", "测试用户", null);
        currentUser.setValue(user);
        
        // 模拟获取书籍和笔记数量
        bookCount.setValue(12);
        noteCount.setValue(5);
        
        isLoading.setValue(false);
    }
} 