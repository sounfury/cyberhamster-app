package org.sounfury.cyber_hamster.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.sounfury.cyber_hamster.data.model.Book;
import org.sounfury.cyber_hamster.data.model.Note;
import org.sounfury.cyber_hamster.data.model.UserBook;
import org.sounfury.cyber_hamster.data.network.request.ReadStatusUpdateDTO;
import org.sounfury.cyber_hamster.data.repository.BookRepository;
import org.sounfury.cyber_hamster.data.repository.NoteRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BookDetailViewModel extends ViewModel {

    private static final String TAG = "BookDetailViewModel";

    private final BookRepository bookRepository;
    private final NoteRepository noteRepository;
    private final MutableLiveData<UserBook> userBook = new MutableLiveData<>();
    private final MutableLiveData<Book> book = new MutableLiveData<>();
    private final MutableLiveData<List<Note>> recentNotes = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> readStatusUpdateSuccess = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    public BookDetailViewModel() {
        this.bookRepository = BookRepository.getInstance();
        this.noteRepository = new NoteRepository();
    }

    public LiveData<UserBook> getUserBook() {
        return userBook;
    }

    public LiveData<Book> getBook() {
        return book;
    }
    
    public LiveData<List<Note>> getRecentNotes() {
        return recentNotes;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getReadStatusUpdateSuccess() {
        return readStatusUpdateSuccess;
    }

    /**
     * 加载图书详情
     *
     * @param bookId 图书ID
     */
    public void loadBookDetail(long bookId) {
        isLoading.setValue(true);

        Disposable disposable = bookRepository.getBookById(bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    isLoading.setValue(false);
                    userBook.setValue(result);
                    if (result != null && result.getBook() != null) {
                        book.setValue(result.getBook());
                        // 加载图书成功后，加载最近笔记
                        loadRecentNotes(bookId);
                    }
                }, throwable -> {
                    isLoading.setValue(false);
                    errorMessage.setValue("获取图书详情失败：" + throwable.getMessage());
                    Log.e(TAG, "加载图书详情失败", throwable);
                });

        disposables.add(disposable);
    }
    
    /**
     * 加载最近笔记
     *
     * @param bookId 图书ID
     */
    public void loadRecentNotes(long bookId) {
        Disposable disposable = noteRepository.getRecentNotes(bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (result != null && result.getData() != null) {
                        recentNotes.setValue(result.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "加载最近笔记失败", throwable);
                    errorMessage.setValue("获取最近笔记失败：" + throwable.getMessage());
                });

        disposables.add(disposable);
    }

    /**
     * 更新图书阅读状态
     *
     * @param userBookId 用户图书ID
     * @param readStatus 阅读状态
     */
    public void updateReadStatus(Long userBookId, Integer readStatus) {
        if (userBookId == null) {
            errorMessage.setValue("图书ID不能为空");
            return;
        }
        
        // 先重置更新状态，避免重复触发
        readStatusUpdateSuccess.setValue(null);
        
        isLoading.setValue(true);
        ReadStatusUpdateDTO updateDTO = new ReadStatusUpdateDTO(userBookId, readStatus);
        
        Disposable disposable = bookRepository.updateReadStatus(updateDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    isLoading.setValue(false);
                    if (result != null && result.isSuccess()) {
                        readStatusUpdateSuccess.setValue(true);
                        // 更新本地UserBook的阅读状态
                        UserBook currentUserBook = userBook.getValue();
                        if (currentUserBook != null) {
                            currentUserBook.setReadStatus(readStatus);
                            userBook.setValue(currentUserBook);
                        }
                    } else {
                        readStatusUpdateSuccess.setValue(false);
                        errorMessage.setValue("更新阅读状态失败：" + (result != null ? result.getMessage() : "未知错误"));
                    }
                }, throwable -> {
                    isLoading.setValue(false);
                    readStatusUpdateSuccess.setValue(false);
                    errorMessage.setValue("更新阅读状态失败：" + throwable.getMessage());
                    Log.e(TAG, "更新阅读状态失败", throwable);
                });
                
        disposables.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
} 