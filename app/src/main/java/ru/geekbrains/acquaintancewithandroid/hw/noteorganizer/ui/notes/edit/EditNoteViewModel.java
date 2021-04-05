package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.notes.edit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.NotesRepository;

public class EditNoteViewModel extends ViewModel {
    private NotesRepository repository;

    public EditNoteViewModel(NotesRepository repository) {
        this.repository = repository;
    }

    private MutableLiveData<Boolean> progress = new MutableLiveData<>(false);

    public LiveData<Boolean> getProgress() {
        return progress;
    }
    private MutableLiveData<Boolean> saveEnabled = new MutableLiveData<>(false);

    public LiveData<Boolean> getSaveEnabled() {
        return saveEnabled;
    }

    private MutableLiveData<Object> saveSucceed = new MutableLiveData<>();

    public LiveData<Object> getSaveSucceed() {
        return saveSucceed;
    }
}
