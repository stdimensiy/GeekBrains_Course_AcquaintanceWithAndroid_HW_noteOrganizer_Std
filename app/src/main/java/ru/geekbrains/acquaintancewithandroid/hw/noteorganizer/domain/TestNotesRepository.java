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
        notes.add(new Note("Второй заголовок который больше", "Тело второй заметки."));
        notes.add(new Note("Третий заголовок", "Тело третьей заметки."));
        notes.add(new Note("Четвертый заголовок", "Тело четвертой заметки."));
        notes.add(new Note("Пятый заголовок", "Тело пятой аметки. Чуть подлиннее"));
        notes.add(new Note("Шестой заголовок длинный", "Тело чшестой заметки ну очень длинное. Многобукаф прям."));
        notes.add(new Note("Седьмой", "Семерка..."));
        notes.add(new Note("Восьмой заголовок 321321321", "Опять тело средней длины с 987897987987 цифрами"));
        notes.add(new Note("Девятый заголовок ", ""));
        notes.add(new Note("Десятый заголовок длинный длинннннннный.....", "Следующая после заметки без тела."));
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
    public void addNewTestNote(CallBack<Note> noteCallBack) {
        //notes.add(new Note(title, "Тело новой заметки."));
        executor.execute((new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(pause/4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Note note = new Note("тестовый заголовок", "Тело новой тестовой заметки.");
                        notes.add(note);
                        noteCallBack.onResult(note);
                    }
                });
            }
        }));
    }

    @Override
    public void deleteNote(Note note, CallBack<Note> noteCallBack) {
        executor.execute((new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(pause/10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        notes.remove(note);
                        noteCallBack.onResult(note);
                    }
                });
            }
        }));
    }

    @Override
    public void clearAllNotes(CallBack<Object> voidCallBack) {
        //создаю временную задержку (для теста) на базе потока
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(pause/2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                notes.clear();
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        voidCallBack.onResult(new Object());
                    }
                });
            }
        });
    }

}
