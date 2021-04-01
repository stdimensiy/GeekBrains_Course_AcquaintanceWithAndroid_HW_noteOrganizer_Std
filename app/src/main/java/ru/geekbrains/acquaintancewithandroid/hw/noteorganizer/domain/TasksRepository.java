package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

import java.util.ArrayList;

public interface TasksRepository {
    void getTasks(CallBack<ArrayList<Task>> callBack);

    void addTask(String title);

    void deleteTask(int index);
}
