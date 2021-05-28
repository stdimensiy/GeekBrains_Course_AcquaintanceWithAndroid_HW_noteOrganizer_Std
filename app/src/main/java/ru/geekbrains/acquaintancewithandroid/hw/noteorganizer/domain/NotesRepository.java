package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

import java.util.ArrayList;

public interface NotesRepository {
    ArrayList<Note> getNotes();
    void addNote(String title);
    void deleteNote(int index);
}
