package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.tasks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.CallBack;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Task;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.TasksRepository;

public class TasksViewModel extends ViewModel {
    private final TasksRepository tasksRepository;
    private MutableLiveData<ArrayList<Task>> tasksLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> tasksProgressBarLiveData = new MutableLiveData<>();

    public TasksViewModel(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    public LiveData<ArrayList<Task>> getTasksLiveData() {
        return tasksLiveData;
    }

    public MutableLiveData<Boolean> getTasksProgressBarLiveData() {
        return tasksProgressBarLiveData;
    }

    public void fetchTasks() {
        //СТАРТ показа прогресс-бара
        tasksProgressBarLiveData.setValue(true);
        tasksRepository.getTasks(new CallBack<ArrayList<Task>>() {
            @Override
            public void onResult(ArrayList<Task> value) {
                tasksLiveData.postValue(value);
                //СТОП показа прогресс-бара
                tasksProgressBarLiveData.setValue(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}