package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

public class Note {
    private String title;               //заголовок заметки
    private String content;             //содержимое заметки
    private long createDate;            //дата и время создания заметки
    private boolean marked;             //флаг состояния заметки (отмечена или не отмечена)

    public Note() {
    }

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }
}
