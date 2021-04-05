package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.notes.edit;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.TestNotesRepository;
public class EditNoteViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EditNoteViewModel(TestNotesRepository.INSTANCE);
    }
}
