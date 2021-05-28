package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TestTasksRepository implements TasksRepository {
    public static final TestTasksRepository INSTANCE = new TestTasksRepository();
    private static final int pause = 2000; // временная пауза в миллисекундах
    private final Executor executor = Executors.newCachedThreadPool();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private ArrayList<Task> tasks = new ArrayList<>();

    private TestTasksRepository() {
        firstIncrement(tasks);
    }

    private void firstIncrement(ArrayList<Task> tasks) {
        tasks.add(new Task("Первая задача", "Описание первой задачи.", false, true));
        tasks.add(new Task("Вторая задача", "", false, false));
        tasks.add(new Task("Третья задача", "Описание третьей задачи.", true, true));
        tasks.add(new Task("Четвертая задача", "", true, false));
        tasks.add(new Task("Пятая задача", "Описание пятой задачи.", false, true));
    }

    @Override
    public void getTasks(CallBack<ArrayList<Task>> callBack) {
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
                        callBack.onResult(tasks);
                    }
                });
            }
        });
    }

    @Override
    public void addTask(String title) {
        tasks.add(new Task(title, "Описание новой задачи.", false, false));
    }

    @Override
    public void deleteTask(int index) {
        tasks.remove(index);
    }
}
