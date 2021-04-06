package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.notes.edit;

import android.text.Editable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.CallBack;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Note;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.NotesRepository;

public class EditNoteViewModel extends ViewModel {
    private final NotesRepository repository;
    private final MutableLiveData<Boolean> progressLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> saveEnabledLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<Object> saveSucceedLiveData = new MutableLiveData<>();

    // конструктор принимает репозиторий (при замене репозитория менять в классе фабрики)
    public EditNoteViewModel(NotesRepository repository) {
        this.repository = repository;
    }

    public LiveData<Boolean> getProgressLiveData() {
        return progressLiveData;
    }

    public LiveData<Boolean> getSaveEnabledLiveData() {
        return saveEnabledLiveData;
    }

    public LiveData<Object> getSaveSucceedLiveData() {
        return saveSucceedLiveData;
    }

    // логика валидации вводимых данных
    // 1 вариант - просто непустая строка
    // 2 вариант (в разработке) проверить, если данные не изменены кнопку не активировать.
    public void validateInput(String newTitle) {
        saveEnabledLiveData.setValue(!newTitle.isEmpty()); // первая проверка, если заголовок заметки не пустой.
    }

    //Логика сохранения измененных данных
    public void saveNote(Editable text, Note note) {
        note.setTitle(text.toString()); // передаем как есть из поля редактирования в элемент
        //СТАРТ показа прогресс-бара
        progressLiveData.setValue(true);
        repository.updateNote(note, new CallBack<Object>() {
            @Override
            public void onResult(Object value) {
                //СТОП показа прогресс-бара
                progressLiveData.setValue(false);
                saveSucceedLiveData.setValue(new Object());
            }
        });
    }
}
