package org.sounfury.cyber_hamster.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.sounfury.cyber_hamster.data.model.Note;
import org.sounfury.cyber_hamster.data.repository.NoteRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteViewModel extends ViewModel {
    
    private final NoteRepository noteRepository;
    private final MutableLiveData<List<Note>> noteList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    
    public NoteViewModel() {
        // 在实际项目中，这应该通过依赖注入提供
        this.noteRepository = new NoteRepository();
        loadNotes();
    }
    
    public LiveData<List<Note>> getNoteList() {
        return noteList;
    }
    
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public void loadNotes() {
        isLoading.setValue(true);
        
        // 在实际应用中，这里应该调用Repository中的异步方法获取数据
        // 此处为简化，直接使用模拟数据
        List<Note> notes = loadMockData();
        noteList.setValue(notes);
        
        isLoading.setValue(false);
    }
    
    // 加载模拟数据
    private List<Note> loadMockData() {
        List<Note> notes = new ArrayList<>();
        
        // 添加一些模拟数据
        notes.add(new Note(1, 1, "《Cracking the Coding Interview》读书笔记", 
                "这本书讲解了许多面试中常见的算法问题...", new Date(), new Date()));
        
        notes.add(new Note(2, 2, "图灵的贡献", 
                "图灵在计算机科学中的贡献是无法估量的...", new Date(), new Date()));
        
        notes.add(new Note(3, 3, "余华《活着》感想", 
                "这本书展现了人在逆境中的坚强...", new Date(), new Date()));
        
        notes.add(new Note(4, 4, "解忧杂货店读后感", 
                "这本书通过一个神奇的杂货店，讲述了几个互不相关的人生故事...", new Date(), new Date()));
        
        return notes;
    }
} 