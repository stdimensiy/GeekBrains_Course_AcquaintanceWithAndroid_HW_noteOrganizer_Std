package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Это фрагмент для настроек");
    }

    public LiveData<String> getText() {
        return mText;
    }
}