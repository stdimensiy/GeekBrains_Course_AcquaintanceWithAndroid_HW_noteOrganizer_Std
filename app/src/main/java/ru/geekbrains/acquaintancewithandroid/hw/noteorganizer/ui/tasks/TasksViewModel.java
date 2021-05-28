package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.tasks;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.CallBack;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Note;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Task;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.TasksRepository;

public class TasksViewModel extends ViewModel {
    private final TasksRepository tasksRepository;
    private final MutableLiveData<Task> newTaskAddedLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> deleteTaskPositionLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Task>> tasksLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> tasksProgressBarLiveData = new MutableLiveData<>();

    public TasksViewModel(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    public MutableLiveData<Task> getNewTaskAddedLiveData() {
        return newTaskAddedLiveData;
    }

    public MutableLiveData<Integer> getDeleteTaskPositionLiveData() {
        return deleteTaskPositionLiveData;
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

    public void addNewTask(Context context, String newTitleTask){
        //СТАРТ показа прогресс-бара
        tasksProgressBarLiveData.setValue(true);
        tasksRepository.addTask(context, newTitleTask, new CallBack<Task>() {
            @Override
            public void onResult(Task value) {
                newTaskAddedLiveData.postValue(value);
                //СТОП показа прогресс-бара
                tasksProgressBarLiveData.setValue(false);
            }
        });
    }

    public void deleteItemPosition(Task task, int contextMenuItemPosition) {
        //СТАРТ показа прогресс-бара
        tasksProgressBarLiveData.setValue(true);
        tasksRepository.deleteTask(task, new CallBack<Task>() {
            @Override
            public void onResult(Task value) {
                deleteTaskPositionLiveData.setValue(contextMenuItemPosition);
                //СТОП показа прогресс-бара
                tasksProgressBarLiveData.setValue(false);
            }
        });
    }

    public void clearAllTasks() {
        tasksProgressBarLiveData.setValue(true);
        tasksRepository.getTasks(new CallBack<ArrayList<Task>>() {
            @Override
            public void onResult(ArrayList<Task> value) {
                for (Task delTask: value) {
                    deleteItemPosition(delTask, 0);
                }
                tasksProgressBarLiveData.setValue(false);
            }
        });
    }
}