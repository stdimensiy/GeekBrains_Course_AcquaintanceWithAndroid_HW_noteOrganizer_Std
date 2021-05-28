package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

import java.util.ArrayList;

public interface NotesRepository {
    void getNotes(CallBack<ArrayList<Note>> callBack);

    void addNewTestNote(CallBack<Note> noteCallBack);

    void deleteNote(Note note, CallBack<Note> noteCallBack);

    void clearAllNotes(CallBack<Object> voidCallBack);

    void updateNote(Note note, CallBack<Object> objectCallBack);

}
