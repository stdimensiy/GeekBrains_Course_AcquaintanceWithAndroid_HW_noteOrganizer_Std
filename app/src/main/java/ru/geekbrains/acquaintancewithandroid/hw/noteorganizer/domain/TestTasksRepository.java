package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

import android.content.Context;
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
        tasks.add(new Task("Первая задача", "Описание первой задачи."));
        tasks.add(new Task("Вторая задача", ""));
        tasks.add(new Task("Третья задача", "Описание третьей задачи."));
        tasks.add(new Task("Четвертая задача", ""));
        tasks.add(new Task("Пятая задача", "Описание пятой задачи."));
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
    public void addTask(Context context, String newTaskTitle, CallBack<Task> taskCallBack) {

    }

    @Override
    public void deleteTask(Task task, CallBack<Task> taskCallBack) {

    }

    @Override
    public void updateTask(Task task, CallBack<Object> objectCallBack) {

    }
}
