package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.notes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.CallBack;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Note;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.NotesRepository;

public class NotesViewModel extends ViewModel {

    private final NotesRepository notesRepository;
    private MutableLiveData<ArrayList<Note>> notesLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> notesProgressBarLiveData = new MutableLiveData<>();

    public NotesViewModel(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    public LiveData<ArrayList<Note>> getNotesLiveData() {
        return notesLiveData;
    }

    public LiveData<Boolean> getNotesProgressBarLiveData() {
        return notesProgressBarLiveData;
    }

    public void fetchNotes() {
        //СТАРТ показа прогресс-бара
        notesProgressBarLiveData.setValue(true);
        notesRepository.getNotes(new CallBack<ArrayList<Note>>() {
            @Override
            public void onResult(ArrayList<Note> value) {
                notesLiveData.postValue(value);
                //СТОП показа прогресс-бара
                notesProgressBarLiveData.setValue(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}