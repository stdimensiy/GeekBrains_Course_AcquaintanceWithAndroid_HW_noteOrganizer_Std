package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.R;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Note;

public class NotesFragment extends Fragment {

    private NotesViewModel notesViewModel;
    private NotesAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notesViewModel = new ViewModelProvider(this, new NotesViewModelFactory()).get(NotesViewModel.class);
        notesViewModel.fetchNotes();
        adapter = new NotesAdapter();
        adapter.setNoteClicked(new NotesAdapter.OnNoteClicked() {
            @Override
            public void onNoteClicked(Note note) {
                Toast.makeText(requireContext(), note.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //подключаем собственно RecyclerView по идентификатору
        RecyclerView notesRecyclerView = view.findViewById(R.id.notes_list);
        notesRecyclerView.setAdapter(adapter);  // подключаем адаптер и ниже сразу менеджер размещения
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        //получаем лайвдату с заметками и начинаем наблюдать за ней
        notesViewModel.getNotesLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Note>>() {
            @Override
            public void onChanged(ArrayList<Note> notes) {
                //работаем с адаптером при каждом изменении
                adapter.clear();                 // зачистка старого состояния адаптера
                adapter.addItems(notes);         // загружаем новые данные
                adapter.notifyDataSetChanged();  // командуем полностью перерисовать вьюшку
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Плавающая кнопка добавления новой замметки нажата!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}