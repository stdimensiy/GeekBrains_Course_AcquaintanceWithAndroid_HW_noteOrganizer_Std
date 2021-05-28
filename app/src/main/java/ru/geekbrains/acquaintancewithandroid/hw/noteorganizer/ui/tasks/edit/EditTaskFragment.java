package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.tasks.edit;

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
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Task;

public class EditTaskFragment extends Fragment {
    private Task editTask;
    private EditTaskViewModel viewModel;
    private OnTaskSaved listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnTaskSaved) {
            listener = (OnTaskSaved) context;
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
        viewModel = new ViewModelProvider(this, new EditTaskViewModelFactory()).get(EditTaskViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editTitle = view.findViewById(R.id.edit_task_EditText_title);
        EditText editContent = view.findViewById(R.id.edit_task_EditText_content);
        if (getArguments() != null) {
            editTask = getArguments().getParcelable("ARG_TASK");
            editTitle.setText(editTask.getTitle());
            editContent.setText(editTask.getContent());
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

        Button buttonSave = view.findViewById(R.id.edit_task_btn_save);
        buttonSave.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.saveTask(editTitle.getText(), editContent.getText(), editTask);
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
        ProgressBar progressBar = view.findViewById(R.id.editTaskProgressBar);
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
                    listener.onTaskSaved();
                }
            }
        });
    }

    public interface OnTaskSaved {
        void onTaskSaved();
    }
}
