package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

import java.util.ArrayList;

public interface TasksRepository {
    ArrayList<Task> getTasks();

    void addTask(String title);

    void deleteTask(int index);
}
