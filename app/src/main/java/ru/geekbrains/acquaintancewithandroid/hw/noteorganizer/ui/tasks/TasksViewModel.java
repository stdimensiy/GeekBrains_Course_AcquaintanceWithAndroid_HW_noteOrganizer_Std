package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.tasks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Task;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.TasksRepository;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.TestTasksRepository;

public class TasksViewModel extends ViewModel {
    private final TasksRepository tasksRepository = TestTasksRepository.INSTANCE;
    private MutableLiveData<ArrayList<Task>> tasksLiveData = new MutableLiveData<>();

    public LiveData<ArrayList<Task>> getTasksLiveData() {
        return tasksLiveData;
    }

    public void fetchTasks() {
        tasksLiveData.setValue(tasksRepository.getTasks());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}