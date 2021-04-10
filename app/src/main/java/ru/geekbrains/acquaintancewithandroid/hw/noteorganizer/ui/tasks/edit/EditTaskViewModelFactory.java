package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.tasks.edit;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.FirestoreTasksRepository;

public class EditTaskViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EditTaskViewModel(FirestoreTasksRepository.INSTANCE);
    }
}
