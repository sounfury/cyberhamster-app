package org.sounfury.cyber_hamster.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.sounfury.cyber_hamster.data.model.User;
import org.sounfury.cyber_hamster.data.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {
    
    private final UserRepository userRepository;
    
    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance(application);
    }
    
    public LiveData<User> getCurrentUser() {
        return userRepository.getCurrentUser();
    }
    
    public LiveData<String> getErrorMessage() {
        return userRepository.getErrorMessage();
    }
    
    public LiveData<Boolean> isLoading() {
        return userRepository.isLoading();
    }
    
    public boolean isLoggedIn() {
        return userRepository.isLoggedIn();
    }
    
    public void login(String username, String password, boolean rememberMe) {
        userRepository.login(username, password, rememberMe);
    }
    
    public void register(String username, String password, String email, String nickname) {
        userRepository.register(username, password, email,nickname);
    }
    
    public void logout() {
        userRepository.logout();
    }
    
    public void checkUsername(String username) {
        userRepository.checkUsername(username);
    }
    
    public void changePassword(String oldPassword, String newPassword) {
        userRepository.changePassword(oldPassword, newPassword);
    }
    
    public void loadSavedCredentials() {
        userRepository.loadSavedCredentials();
    }
} 