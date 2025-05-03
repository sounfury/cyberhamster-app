package org.sounfury.cyber_hamster.data.repository;

import org.sounfury.cyber_hamster.data.model.Note;
import org.sounfury.cyber_hamster.data.network.RetrofitClient;
import org.sounfury.cyber_hamster.data.network.api.NoteService;
import org.sounfury.cyber_hamster.data.network.response.Result;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class NoteRepository {
    
    private final NoteService noteService;
    
    public NoteRepository() {
        this.noteService = RetrofitClient.getInstance().getNoteService();
    }
    
    /**
     * 获取所有笔记（游标分页）
     * @param lastId 上一页最后一个笔记的ID，首次加载传null
     */
    public Observable<Result<List<Note>>> getAllNotes(Long lastId) {
        return noteService.getAllNotes(lastId);
    }
    
    /**
     * 根据书籍ID获取笔记
     */
    public Observable<Result<List<Note>>> getNotesByBookId(long bookId) {
        return noteService.searchNotes(bookId, null, null, null);
    }
    
    /**
     * 获取最近的笔记
     */
    public Observable<Result<List<Note>>> getRecentNotes(long bookId) {
        return noteService.getRecentNotes(bookId);
    }
    
    /**
     * 根据笔记ID获取笔记详情
     */
    public Observable<Result<Note>> getNoteById(long noteId) {
        return noteService.getNoteById(noteId);
    }
    
    /**
     * 保存或更新笔记
     * 当note.id为0或null时为创建，否则为更新
     */
    public Observable<Result<Void>> saveNote(Note note) {
        return noteService.saveNote(note);
    }
    
    /**
     * 删除笔记
     */
    public Observable<Result<Void>> deleteNote(long noteId) {
        return noteService.deleteNote(noteId);
    }
    
    /**
     * 搜索笔记
     */
    public Observable<Result<List<Note>>> searchNotes(Long bookId, String content, String startTime, String endTime) {
        return noteService.searchNotes(bookId, content, startTime, endTime);
    }
} 