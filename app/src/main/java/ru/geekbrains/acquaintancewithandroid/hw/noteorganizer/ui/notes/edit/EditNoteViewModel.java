package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.notes.edit;

import androidx.lifecycle.ViewModel;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.NotesRepository;

public class EditNoteViewModel extends ViewModel {
    private NotesRepository repository;

    public EditNoteViewModel(NotesRepository repository) {
        this.repository = repository;
    }
}
