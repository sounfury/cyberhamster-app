package org.sounfury.cyber_hamster.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.sounfury.cyber_hamster.data.model.Note;
import org.sounfury.cyber_hamster.data.network.api.NoteService;
import org.sounfury.cyber_hamster.data.network.response.Result;
import org.sounfury.cyber_hamster.data.repository.NoteRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NoteViewModel extends ViewModel {
    
    private final NoteRepository noteRepository;
    private final MutableLiveData<List<Note>> noteList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> saveNoteResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingMore = new MutableLiveData<>();
    private final MutableLiveData<Boolean> hasMoreData = new MutableLiveData<>(true);
    
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    
    private final MutableLiveData<Note> note = new MutableLiveData<>();
    
    public NoteViewModel() {
        // 在实际项目中，这应该通过依赖注入提供
        this.noteRepository = new NoteRepository();
        loadNotes(null);
    }
    
    public LiveData<List<Note>> getNoteList() {
        return noteList;
    }
    
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public LiveData<Boolean> getSaveNoteResult() {
        return saveNoteResult;
    }
    
    public LiveData<Note> getNote() {
        return note;
    }
    
    public LiveData<Boolean> getIsLoadingMore() {
        return isLoadingMore;
    }
    
    public LiveData<Boolean> getHasMoreData() {
        return hasMoreData;
    }
    
    public void loadNotes(Long lastId) {
        if (lastId == null) {
            isLoading.setValue(true);
        } else {
            isLoadingMore.setValue(true);
        }
        
        Disposable disposable = noteRepository.getAllNotes(lastId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            if (result != null && result.getData() != null) {
                                List<Note> data = result.getData();
                                if (data.isEmpty()) {
                                    hasMoreData.setValue(false);
                                } else {
                                    if (lastId == null) {
                                        noteList.setValue(data);
                                    } else {
                                        List<Note> currentList = noteList.getValue();
                                        if (currentList != null) {
                                            currentList.addAll(data);
                                            noteList.setValue(currentList);
                                        }
                                    }
                                }
                            }
                            
                            if (lastId == null) {
                                isLoading.setValue(false);
                            } else {
                                isLoadingMore.setValue(false);
                            }
                        },
                        error -> {
                            errorMessage.setValue("加载笔记失败: " + error.getMessage());
                            if (lastId == null) {
                                isLoading.setValue(false);
                            } else {
                                isLoadingMore.setValue(false);
                            }
                        }
                );
        
        compositeDisposable.add(disposable);
    }
    
    public void loadMoreNotes(Long lastId) {
        if (lastId != null && Boolean.TRUE.equals(hasMoreData.getValue())) {
            loadNotes(lastId);
        }
    }
    
    public void refreshNotes() {
        hasMoreData.setValue(true);
        loadNotes(null);
    }
    
    /**
     * 保存或更新笔记
     * 当note.id为0或null时为创建，否则为更新
     */
    public void saveNote(Note note) {
        isLoading.setValue(true);
        
        Disposable disposable = noteRepository.saveNote(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            saveNoteResult.setValue(true);
                            isLoading.setValue(false);
                        },
                        error -> {
                            errorMessage.setValue("保存笔记失败: " + error.getMessage());
                            saveNoteResult.setValue(false);
                            isLoading.setValue(false);
                        }
                );
        
        compositeDisposable.add(disposable);
    }
    
    /**
     * 根据书籍ID获取笔记
     */
    public void getNotesByBookId(long bookId) {
        isLoading.setValue(true);
        
        Disposable disposable = noteRepository.getNotesByBookId(bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            noteList.setValue(result.getData());
                            isLoading.setValue(false);
                        },
                        error -> {
                            errorMessage.setValue("加载笔记失败: " + error.getMessage());
                            isLoading.setValue(false);
                        }
                );
        
        compositeDisposable.add(disposable);
    }
    
    /**
     * 根据ID加载笔记
     */
    public void loadNoteById(long noteId) {
        isLoading.setValue(true);
        
        Disposable disposable = noteRepository.getNoteById(noteId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            if (result != null && result.getData() != null) {
                                note.setValue(result.getData());
                            }
                            isLoading.setValue(false);
                        },
                        error -> {
                            errorMessage.setValue("加载笔记失败: " + error.getMessage());
                            isLoading.setValue(false);
                        }
                );
        
        compositeDisposable.add(disposable);
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
} 