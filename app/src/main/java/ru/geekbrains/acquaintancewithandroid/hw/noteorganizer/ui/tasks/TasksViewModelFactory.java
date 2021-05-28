package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.tasks;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.TestTasksRepository;

public class TasksViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TasksViewModel(TestTasksRepository.INSTANCE);
    }
}
