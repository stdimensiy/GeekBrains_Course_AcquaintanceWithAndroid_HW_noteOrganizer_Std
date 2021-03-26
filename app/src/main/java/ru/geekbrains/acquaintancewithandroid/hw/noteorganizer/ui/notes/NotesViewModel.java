package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.notes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Note;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.NotesRepository;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.TestNotesRepository;

public class NotesViewModel extends ViewModel {

    private final NotesRepository notesRepository = TestNotesRepository.INSTANCE;
    private MutableLiveData<ArrayList<Note>> notesLiveData = new MutableLiveData<>();

    public LiveData<ArrayList<Note>> getNotesLiveData() {
        return notesLiveData;
    }

    public void fetchNotes() {
        notesLiveData.setValue(notesRepository.getNotes());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}