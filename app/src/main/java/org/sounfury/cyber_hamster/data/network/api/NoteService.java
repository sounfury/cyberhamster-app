package org.sounfury.cyber_hamster.data.network.api;

import org.sounfury.cyber_hamster.data.model.Note;
import org.sounfury.cyber_hamster.data.network.response.Result;


import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Query;

public interface NoteService {

    String BASE_URL = "/api/user-note";
    //创建或更新笔记（当note.id为0或null时为创建，否则为更新）
    @POST(BASE_URL)
    Observable<Result<Void>> saveNote(@Body Note note);

    //删除笔记
    @DELETE(BASE_URL + "/{noteId}")
    Observable<Result<Void>> deleteNote(@Path("noteId") long noteId);

    //获取所有笔记,游标分页，根据lastId
    @GET(BASE_URL+"/list")
    Observable<Result<List<Note>>> getAllNotes(@Query("lastId") Long lastId);

    //搜索，根据bookId或者title或者content或者createTime
    @GET(BASE_URL + "/search")
    Observable<Result<List<Note>>> searchNotes(
            @Query("bookId") Long bookId,
            @Query("content") String content,
            @Query("startTime") String startTime,
            @Query("endTime") String endTime
    );

    //获取最近的3条笔记
    @GET(BASE_URL + "/book/{bookId}/recent")
    Observable<Result<List<Note>>> getRecentNotes(@Path("bookId") long bookId);

    //获取笔记详情
    @GET(BASE_URL + "/{noteId}")
    Observable<Result<Note>> getNoteById(@Path("noteId") long noteId);


}