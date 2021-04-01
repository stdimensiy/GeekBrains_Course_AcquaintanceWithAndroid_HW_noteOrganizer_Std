package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

import java.util.ArrayList;

public interface NotesRepository {
    void getNotes(CallBack<ArrayList<Note>> callBack);

    void addNote(String title);

    void deleteNote(int index);
}
