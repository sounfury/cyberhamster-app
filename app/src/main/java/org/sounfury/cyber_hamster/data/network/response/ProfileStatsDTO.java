package org.sounfury.cyber_hamster.data.network.response;

public class ProfileStatsDTO {
    public long getBookCount() {
        return bookCount;
    }

    public void setBookCount(long bookCount) {
        this.bookCount = bookCount;
    }

    public long getNoteCount() {
        return noteCount;
    }

    public ProfileStatsDTO(long bookCount, long noteCount) {
        this.bookCount = bookCount;
        this.noteCount = noteCount;
    }

    public void setNoteCount(long noteCount) {
        this.noteCount = noteCount;
    }

    /**
     * 图书数量
     */
    private long bookCount;
    
    /**
     * 笔记数量
     */
    private long noteCount;
}
