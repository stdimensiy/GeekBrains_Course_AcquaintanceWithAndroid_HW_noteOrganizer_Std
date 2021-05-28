package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.R;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Note;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.Pluggable;

public class NotesFragment extends Fragment implements Pluggable {
    public static final String TAG = "NOTES_LIST";

    private NotesViewModel notesViewModel;
    private NotesAdapter adapter;
    private int contextMenuItemPosition;
    private OnNoteSelected listener;

    @Override
    public void onStart() {
        super.onStart();
        notesViewModel.fetchNotes();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnNoteSelected) {
            listener = (OnNoteSelected) context;
        }
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notesViewModel = new ViewModelProvider(this, new NotesViewModelFactory()).get(NotesViewModel.class);
        notesViewModel.fetchNotes();
        adapter = new NotesAdapter(this);
        adapter.setNoteClicked(new NotesAdapter.OnNoteClicked() {
            @Override
            public void onNoteClicked(Note note) {
                // реализуем единую форму для просмотра заметки и для ее редактирования при необходимости
                if (listener != null) {
                    listener.onNoteSelected(note);
                }
                //Toast.makeText(requireContext(), note.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        // для длительного нажатия на элемент
        adapter.setNoteLongClicked(new NotesAdapter.OnNoteLongClicked() {
            @Override
            public void onNoteLongClicked(View itemView, int position, Note note) {
                contextMenuItemPosition = position;
                // интересная конструкция применения части кода в зависимости от версии SDK (надо запомнить и применять)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    itemView.showContextMenu(10.f, 10.f);
                } else {
                    itemView.showContextMenu();
                }
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
        notesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //подключаем прогрессбар (круговой)
        ProgressBar progressBar = view.findViewById(R.id.notesProgressBar);
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
        //получаем лайвдату прогресс-бара (фрагмента списка заметок) и начинаем наблюдать за ним
        notesViewModel.getNotesProgressBarLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isVisible) {
                if (isVisible) {  // если прогресс должен быть виден отображаем
                    progressBar.setVisibility(View.VISIBLE);
                } else {        // когда он  не должен быть виден закрываем полностью.
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        //получаем лайвдату с новой заметкой внутри
        notesViewModel.getNewNoteAddedLiveData().observe(getViewLifecycleOwner(), new Observer<Note>() {
            @Override
            public void onChanged(Note note) {
                //работаем с адаптером при каждом изменении
                adapter.addItem(note);         // загружаем новые данные
                adapter.notifyItemInserted(adapter.getItemCount() - 1);
                notesRecyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);

            }
        });

        //получаем лайвдату удаления заметки
        notesViewModel.getDeleteNotePositionLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer position) {
                //работаем с адаптером при каждом изменении
                adapter.deleteItem(position);
                adapter.notifyItemRemoved(position);
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Плавающая кнопка добавления новой замметки нажата!", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                notesViewModel.addNewNote();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.notes_options_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // обработка нажатия конкретных пунктов меню.
            // позиционирование происходит по идентификатору (наименование игнорируется и может быть
            // любым и на любом языке)
            // новые пункты именю или идентификаторы обработчики к ктороым не реализованы игнорируются.
            case R.id.action_new_note:
                //Командуем добавить новую заметку
                notesViewModel.addNewNote();
                break;
            case R.id.action_new_theme:
                Pluggable.ToastPlug(requireContext(), "Добавление новой темы");
                break;
            case R.id.action_settings:
                Pluggable.ToastPlug(requireContext(), "Настройки заметок");
                break;
            case R.id.action_help:
                Pluggable.ToastPlug(requireContext(), "Инструкция для заметок");
                break;
            case R.id.action_clear_all_notes:
                //Командуем очистить весь список полностью
                notesViewModel.clearAllNotes();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = requireActivity().getMenuInflater();
        menuInflater.inflate(R.menu.notes_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete_note) {
            notesViewModel.deleteItemPosition(adapter.getItemAtIndex(contextMenuItemPosition), contextMenuItemPosition);
        }
        return super.onContextItemSelected(item);
    }

    public interface OnNoteSelected {
        void onNoteSelected(Note note);
    }
}