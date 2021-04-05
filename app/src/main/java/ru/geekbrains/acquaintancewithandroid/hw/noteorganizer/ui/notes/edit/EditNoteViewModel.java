package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.notes.edit;

import android.text.Editable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.CallBack;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Note;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.NotesRepository;

public class EditNoteViewModel extends ViewModel {
    private NotesRepository repository;
    private MutableLiveData<Boolean> progress = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> saveEnabled = new MutableLiveData<>(false);
    private MutableLiveData<Object> saveSucceed = new MutableLiveData<>();

    public EditNoteViewModel(NotesRepository repository) {
        this.repository = repository;
    }

    public LiveData<Boolean> getProgress() {
        return progress;
    }

    public LiveData<Boolean> getSaveEnabled() {
        return saveEnabled;
    }

    public LiveData<Object> getSaveSucceed() {
        return saveSucceed;
    }

    public void validateInput(String newTitle) {
        saveEnabled.setValue(!newTitle.isEmpty()); // первая проверка, если заголовок заметки не пустой.
    }

    public void saveNote(Editable text, Note note) {
        note.setTitle(text.toString()); // передаем как есть из поля редактирования в элемент
        //СТАРТ показа прогресс-бара
        progress.setValue(true);
        repository.updateNote(note, new CallBack<Object>() {
            @Override
            public void onResult(Object value) {
                //СТОП показа прогресс-бара
                progress.setValue(false);
                saveSucceed.setValue(new Object());
            }
        });
    }
}
