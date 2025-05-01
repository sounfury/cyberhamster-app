package org.sounfury.cyber_hamster.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.sounfury.cyber_hamster.data.network.request.InboundInput;
import org.sounfury.cyber_hamster.data.repository.BookRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddBookViewModel extends ViewModel {
    
    private BookRepository bookRepository;
    private CompositeDisposable disposables = new CompositeDisposable();
    
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> addSuccess = new MutableLiveData<>();
    
    public AddBookViewModel() {
        bookRepository = BookRepository.getInstance();
    }
    
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public LiveData<Boolean> getAddSuccess() {
        return addSuccess;
    }
    
    /**
     * 图书入库
     * @param isbn ISBN码
     * @param storageLocation 物理位置（可选）
     * @param remark 备注（可选）
     */
    public void addBook(String isbn, String storageLocation, String remark) {
        if (isbn == null || isbn.isEmpty()) {
            errorMessage.setValue("ISBN不能为空");
            return;
        }
        
        isLoading.setValue(true);
        
        // 创建入库请求体
        InboundInput inboundInput = new InboundInput(isbn, storageLocation, remark);
        
        disposables.add(bookRepository.inboundBook(inboundInput)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            isLoading.setValue(false);
                            if (result.isSuccess()) {
                                addSuccess.setValue(true);
                            } else {
                                errorMessage.setValue(result.getMessage());
                            }
                        },
                        throwable -> {
                            isLoading.setValue(false);
                            errorMessage.setValue("网络请求失败: " + throwable.getMessage());
                        }
                ));
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
} 