package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.notes;

import android.content.Context;

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

    public NotesViewModel(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    public MutableLiveData<Integer> getDeleteNotePositionLiveData() {
        return deleteNotePositionLiveData;
    }

    public LiveData<Note> getNewNoteAddedLiveData() {
        return newNoteAddedLiveData;
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

    public void addNewNote(Context context, String newTitle, String newContent) {
        //СТАРТ показа прогресс-бара
        notesProgressBarLiveData.setValue(true);
        notesRepository.addNewTestNote(context, newTitle, newContent, new CallBack<Note>() {
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
        notesRepository.getNotes(new CallBack<ArrayList<Note>>() {
            @Override
            public void onResult(ArrayList<Note> value) {
                for (Note delNote :
                        value) {
                    deleteItemPosition(delNote, 0);
                }
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
    }
}