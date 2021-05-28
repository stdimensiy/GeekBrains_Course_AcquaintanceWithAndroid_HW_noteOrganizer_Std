package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TestNotesRepository implements NotesRepository {
    public static final TestNotesRepository INSTANCE = new TestNotesRepository();
    private static final int pause = 2000; // временная пауза в миллисекундах
    private final Executor executor = Executors.newCachedThreadPool();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private ArrayList<Note> notes = new ArrayList<>();

    private TestNotesRepository() {
        firstIncrement(notes);
    }

    private void firstIncrement(ArrayList<Note> notes) {
        notes.add(new Note("Первый заголовок", "Тело первой заметки."));
        notes.add(new Note("Второй заголовок", "Тело второй заметки."));
        notes.add(new Note("Третий заголовок", "Тело третьей заметки."));
        notes.add(new Note("Четвертый заголовок", "Тело четвертой аметки."));
    }

    @Override
    public void getNotes(CallBack<ArrayList<Note>> callBack) {
        //создаю временную задержку (для теста) на базе потока
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(pause);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onResult(notes);
                    }
                });
            }
        });
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
