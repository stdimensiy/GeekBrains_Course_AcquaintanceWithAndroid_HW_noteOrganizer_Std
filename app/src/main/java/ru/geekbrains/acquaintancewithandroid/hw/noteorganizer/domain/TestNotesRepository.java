package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

import java.util.ArrayList;

public class TestNotesRepository implements NotesRepository {
    public static final TestNotesRepository INSTANCE = new TestNotesRepository();

    ArrayList<Note> notes = new ArrayList<>();

    private TestNotesRepository(){
        firstIncrement(notes);
    }

    private void firstIncrement(ArrayList<Note> notes) {
        notes.add(new Note("Первый заголовок", "Тело первой заметки."));
        notes.add(new Note("Второй заголовок", "Тело второй заметки."));
        notes.add(new Note("Третий заголовок", "Тело третьей заметки."));
        notes.add(new Note("Четвертый заголовок", "Тело четвертой аметки."));
    }

    @Override
    public ArrayList<Note> getNotes() {
        return notes;
    }

    @Override
    public void addNote(String title) {
        notes.add(new Note(title, "Тело новой заметки."));
    }

    @Override
    public void deleteNote(int index) {
        notes.remove(index);
    }

}
