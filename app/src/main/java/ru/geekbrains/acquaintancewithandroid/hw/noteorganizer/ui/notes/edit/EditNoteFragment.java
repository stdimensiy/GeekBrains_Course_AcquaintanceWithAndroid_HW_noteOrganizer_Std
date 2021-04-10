package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.notes.edit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.R;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Note;

public class EditNoteFragment extends Fragment {
    private Note editNote;
    private EditNoteViewModel viewModel;
    private OnNoteSaved listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnNoteSaved) {
            listener = (OnNoteSaved) context;
        }
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
        // реализация принудительного сокрытия экранной клавиатуры по завершении редактирования заметки
        Activity activity = getActivity();
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        EditText editTitle = view.findViewById(R.id.edit_note_EditText_title);
        EditText editContent = view.findViewById(R.id.edit_note_EditText_content);
        if (getArguments() != null) {
            editNote = getArguments().getParcelable("ARG_NOTE");
            editTitle.setText(editNote.getTitle());
            editContent.setText(editNote.getContent());
        }
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

        editContent.addTextChangedListener(new TextWatcher() {
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

        Button buttonSave = view.findViewById(R.id.edit_note_btn_save);
        buttonSave.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.saveNote(editTitle.getText(), editContent.getText(), editNote);
            }
        }));

        //подписываем на событие "сохранение разрешено"
        viewModel.getSaveEnabledLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                buttonSave.setEnabled(aBoolean);
            }
        });

        //подписываем на событие Прогессбара
        ProgressBar progressBar = view.findViewById(R.id.editNotesProgressBar);
        viewModel.getProgressLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        //подписываем на сигнал об успешном сохранении
        viewModel.getSaveSucceedLiveData().observe(getViewLifecycleOwner(), new Observer<Object>() {
            @Override
            public void onChanged(Object o) {
                // Сигнал об успешном сохранении данных
                if (listener != null) {
                    listener.onNoteSaved();
                }
            }
        });
    }

    public interface OnNoteSaved {
        void onNoteSaved();
    }
}
