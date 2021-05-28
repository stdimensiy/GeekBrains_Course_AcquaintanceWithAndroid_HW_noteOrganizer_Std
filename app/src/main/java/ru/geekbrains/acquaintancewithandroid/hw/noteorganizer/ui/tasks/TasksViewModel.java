package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.tasks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TasksViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TasksViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Это фрагмент для задач.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}