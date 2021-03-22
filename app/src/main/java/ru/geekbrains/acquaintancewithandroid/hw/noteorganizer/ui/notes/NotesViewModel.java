package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.notes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NotesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Это фрагмент для записей.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}