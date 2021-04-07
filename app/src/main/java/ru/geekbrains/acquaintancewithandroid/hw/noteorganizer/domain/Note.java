package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    public String id;                   //уникальный идентификатор записи
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

    protected Note(Parcel in) {
        title = in.readString();
        content = in.readString();
        createDate = in.readLong();
        marked = in.readByte() != 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeLong(createDate);
        dest.writeByte((byte) (marked ? 1 : 0));
    }
}
