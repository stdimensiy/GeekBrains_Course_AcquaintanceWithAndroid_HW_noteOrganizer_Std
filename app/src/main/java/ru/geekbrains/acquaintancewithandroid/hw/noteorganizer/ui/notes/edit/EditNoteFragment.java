package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.notes.edit;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.R;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Note;

public class EditNoteFragment extends Fragment {
    public static final String TAG = "EditNoteFragment";
    public static final String ARG_NOTE = "ARG_NOTE";
    private Note note;
    private EditNoteViewModel viewModel;
    private OnNoteSaved listener;

    public static EditNoteFragment newInstance(Note note) {
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_NOTE, note);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnNoteSaved){
            listener = (OnNoteSaved) context;
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
        note = getArguments().getParcelable(ARG_NOTE);
        viewModel = new ViewModelProvider(this, new EditNoteViewModelFactory()).get(EditNoteViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbarEditNote);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed(); // по кнопке "назад" быстрый выход из режима редактирования без сохранения
            }
        });
        Button buttonSave = view.findViewById(R.id.edit_note_btn_save);
        EditText editTitle = view.findViewById(R.id.edit_note_EditText_title);
        buttonSave.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.saveNote(editTitle.getText(), note);
            }
        }));

        editTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.validateInput(s.toString());
            }
        });

        editTitle.setText(note.getTitle()); // заполняем поле заголовка заметки текущим значением заголовка заметки

        //подписываем на событие "сохранение разрешено"
        viewModel.getSaveEnabled().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                buttonSave.setEnabled(aBoolean);
            }
        });

        //подписываем на событие Прогессбара
        ProgressBar progressBar = view.findViewById(R.id.editNotesProgressBar);
        viewModel.getProgress().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        viewModel.getSaveSucceed().observe(getViewLifecycleOwner(), new Observer<Object>() {
            @Override
            public void onChanged(Object o) {
                // Сигнал об успешном сохранении данных
                if (listener != null){
                    listener.onNoteSaved();
                }
            }
        });
    }

    public interface OnNoteSaved {
        void onNoteSaved();
    }
}
