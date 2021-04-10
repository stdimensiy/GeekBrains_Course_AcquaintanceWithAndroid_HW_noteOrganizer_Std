package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

import android.content.Context;

import java.util.ArrayList;

public interface TasksRepository {
    void getTasks(CallBack<ArrayList<Task>> callBack);

    void addTask(Context context, CallBack<Task> taskCallBack);

    void deleteTask(Task task, CallBack<Task> taskCallBack);

    void updateTask(Task task, CallBack<Object> objectCallBack);
}
