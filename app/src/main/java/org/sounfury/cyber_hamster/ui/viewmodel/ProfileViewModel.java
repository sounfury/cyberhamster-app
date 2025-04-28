package org.sounfury.cyber_hamster.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.sounfury.cyber_hamster.data.model.User;
import org.sounfury.cyber_hamster.data.repository.UserRepository;

public class ProfileViewModel extends ViewModel {
    private final UserRepository userRepository;

    private final MutableLiveData<User> currentUser = new MutableLiveData<>();
    private final MutableLiveData<Integer> bookCount = new MutableLiveData<>();
    private final MutableLiveData<Integer> noteCount = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        userRepository = UserRepository.getInstance(application);
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

} 