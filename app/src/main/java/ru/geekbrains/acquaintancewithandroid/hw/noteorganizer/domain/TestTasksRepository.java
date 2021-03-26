package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

import java.util.ArrayList;

public class TestTasksRepository implements TasksRepository{
    private static final TestTasksRepository INSTANCE = new TestTasksRepository();
    ArrayList<Task> tasks = new ArrayList<>();

    private TestTasksRepository (){

    }

    private void firstIncrement(ArrayList<Task> tasks) {
        tasks.add(new Task("Первая задача", "Описание первой задачи.", false, true));
        tasks.add(new Task("Вторая задача", "", false, false));
        tasks.add(new Task("Третья задача", "Описание третьей задачи.", true, true));
        tasks.add(new Task("Четвертая задача", "", true, false));
        tasks.add(new Task("Пятая задача", "Описание пятой задачи.", false, true));
    }

    @Override
    public ArrayList<Task> getTasks() {
        return tasks;
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
