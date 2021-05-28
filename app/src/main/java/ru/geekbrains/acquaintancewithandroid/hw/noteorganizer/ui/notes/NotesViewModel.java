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
    //временная мера для урока №10
    private final MutableLiveData<Note> newNoteAddedLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> deleteNotePositionLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Note>> notesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> notesProgressBarLiveData = new MutableLiveData<>();

    public MutableLiveData<Integer> getDeleteNotePositionLiveData() {
        return deleteNotePositionLiveData;
    }

    public LiveData<Note> getNewNoteAddedLiveData() {
        return newNoteAddedLiveData;
    }

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

    public void addNewNote() {
        //СТАРТ показа прогресс-бара
        notesProgressBarLiveData.setValue(true);
        notesRepository.addNewTestNote(new CallBack<Note>() {
            @Override
            public void onResult(Note value) {
                newNoteAddedLiveData.postValue(value);
                //СТОП показа прогресс-бара
                notesProgressBarLiveData.setValue(false);
            }
        });
    }

    public void clearAllNotes() {
        //СТАРТ показа прогресс-бара
        notesProgressBarLiveData.setValue(true);
        notesRepository.clearAllNotes(new CallBack<Object>() {
            @Override
            public void onResult(Object value) {
                notesLiveData.postValue(new ArrayList<>());
                //СТОП показа прогресс-бара
                notesProgressBarLiveData.setValue(false);
            }
        });
    }

    public void deleteItemPosition(Note note, int contextMenuItemPosition) {
        //СТАРТ показа прогресс-бара
        notesProgressBarLiveData.setValue(true);
        notesRepository.deleteNote(note, new CallBack<Note>() {
            @Override
            public void onResult(Note value) {
                deleteNotePositionLiveData.setValue(contextMenuItemPosition);
                //СТОП показа прогресс-бара
                notesProgressBarLiveData.setValue(false);
            }
        });

        //deleteNotePositionLiveData.setValue(contextMenuItemPosition);
    }
}