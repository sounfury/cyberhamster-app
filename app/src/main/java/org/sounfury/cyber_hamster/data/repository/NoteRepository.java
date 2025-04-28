package org.sounfury.cyber_hamster.data.repository;

import org.sounfury.cyber_hamster.data.model.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteRepository {
    
    // 获取所有笔记
    public List<Note> getAllNotes() {
        // 实际项目中，这里应该从网络或数据库获取数据
        // 此处使用模拟数据
        return getMockNotes();
    }
    
    // 根据书籍ID获取笔记
    public List<Note> getNotesByBookId(int bookId) {
        List<Note> allNotes = getMockNotes();
        List<Note> filteredNotes = new ArrayList<>();
        
        for (Note note : allNotes) {
            if (note.getBookId() == bookId) {
                filteredNotes.add(note);
            }
        }
        
        return filteredNotes;
    }
    
    // 根据ID获取笔记
    public Note getNoteById(int id) {
        List<Note> notes = getMockNotes();
        for (Note note : notes) {
            if (note.getId() == id) {
                return note;
            }
        }
        return null;
    }
    
    // 添加笔记
    public boolean addNote(Note note) {
        // 实现添加笔记的逻辑
        return true;
    }
    
    // 更新笔记
    public boolean updateNote(Note note) {
        // 实现更新笔记的逻辑
        return true;
    }
    
    // 删除笔记
    public boolean deleteNote(int id) {
        // 实现删除笔记的逻辑
        return true;
    }
    
    // 获取模拟数据
    private List<Note> getMockNotes() {
        List<Note> notes = new ArrayList<>();
        
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