package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.R;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Pluggable;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Task;

public class TasksFragment extends Fragment {

    private TasksViewModel tasksViewModel;
    private TasksAdapter adapter;
    private int contextMenuItemPosition;
    private OnTaskSelected listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnTaskSelected) {
            listener = (OnTaskSelected) context;
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
        tasksViewModel = new ViewModelProvider(this, new TasksViewModelFactory()).get(TasksViewModel.class);
        tasksViewModel.fetchTasks();
        adapter = new TasksAdapter(this);
        adapter.setTaskClicked(new TasksAdapter.OnTaskClicked() {
            @Override
            public void onTaskClicked(Task task) {
                if (listener != null) {
                    listener.onTaskSelected(task);
                }
                //Toast.makeText(requireContext(), task.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        // для длительного нажатия на элемент
        adapter.setTaskLongClicked(new TasksAdapter.OnTaskLongClicked() {
            @Override
            public void onTaskLongClicked(View itemView, int position, Task task) {
                contextMenuItemPosition = position;
                // интересная конструкция применения части кода в зависимости от версии SDK (надо запомнить и применять)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    itemView.showContextMenu(10.f, 10.f);
                } else {
                    itemView.showContextMenu();
                }
            }
        });
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerForContextMenu(view);
        RecyclerView taskRecyclerView = view.findViewById(R.id.tasks_list);
        taskRecyclerView.setAdapter(adapter);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        //подключаем прогрессбар (круговой)
        ProgressBar progressBar = view.findViewById(R.id.tasksProgressBar);
        tasksViewModel.getTasksLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Task>>() {
            @Override
            public void onChanged(ArrayList<Task> tasks) {
                adapter.clear();
                adapter.addItems(tasks);
                adapter.notifyDataSetChanged();
            }
        });
        //получаем лайвдату прогресс-бара (фрагмента списка задач) и начинаем наблюдать за ним
        tasksViewModel.getTasksProgressBarLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isVisible) {
                if (isVisible) {  // если прогресс должен быть виден отображаем
                    progressBar.setVisibility(View.VISIBLE);
                } else {        // когда он  не должен быть виден закрываем полностью.
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        //получаем лайвдату с новой задачей внутри
        tasksViewModel.getNewTaskAddedLiveData().observe(getViewLifecycleOwner(), new Observer<Task>() {
            @Override
            public void onChanged(Task task) {
                //работаем с адаптером при каждом изменении
                adapter.addItem(task);         // загружаем новые данные
                adapter.notifyItemInserted(adapter.getItemCount() - 1);
                taskRecyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
            }
        });
        //получаем лайвдату удаления заметки
        tasksViewModel.getDeleteTaskPositionLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
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
                //tasksViewModel.addNewTask(requireContext());
                showDialogInputTitleNewTask();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.tasks_options_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // обработка нажатия конкретных пунктов меню.
            // позиционирование происходит по идентификатору (наименование игнорируется и может быть
            // любым и на любом языке)
            // новые пункты именю или идентификаторы обработчики к ктороым не реализованы игнорируются.
            case R.id.action_new_task:
                tasksViewModel.addNewTask(requireContext(),"");
                break;
            case R.id.action_new_type:
                Pluggable.toastPlug(requireContext(), "Добавление нового типа задачи");
                break;
            case R.id.action_settings:
                Pluggable.toastPlug(requireContext(), "Настройки задач");
                break;
            case R.id.action_help:
                Pluggable.toastPlug(requireContext(), "Инструкция для задач");
                break;
            case R.id.action_clear_all_tasks:
                showAlertDeleteAllTasks();
                //Pluggable.toastPlug(requireContext(), "Удалить все задачи");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        //Log.w("TASKS - FRAGMENT", "сработало событие onCreateContextMenu");
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = requireActivity().getMenuInflater();
        menuInflater.inflate(R.menu.tasks_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete_task) {
            tasksViewModel.deleteItemPosition(adapter.getItemAtIndex(contextMenuItemPosition), contextMenuItemPosition);
            // Toast.makeText(requireContext(), "Тестовый тост", Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }

    private void showAlertDeleteAllTasks() {
        AlertDialog firstAlert = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.alert_title_warning)
                .setMessage(R.string.tasks_alert_delete_all_message)
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setPositiveButton(R.string.text_answer_is_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tasksViewModel.clearAllTasks();
                        //Toast.makeText(requireContext(), "Пользователь ответил ДА!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.text_answer_is_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // При отрицательном ответе выполнение метода удаления всех заданий отменяется
                        //Toast.makeText(requireContext(), "Пользователь ответил НЕТ!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setCancelable(false)
                .create();
        firstAlert.show();
    }

    private void showDialogInputTitleNewTask() {
        EditText newTitleTask = (EditText) getLayoutInflater().inflate(R.layout.single_edittext_dialog, null);
        AlertDialog inputTitle = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.task_dialog_input_title_title)
                .setView(newTitleTask)
                .setIcon(R.drawable.ic_baseline_input_24)
                .setPositiveButton(R.string.task_dialog_create_new_btn_ok_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tasksViewModel.addNewTask(requireContext(), newTitleTask.getText().toString());
                        //Toast.makeText(requireContext(), "Пользователь ввел: " + newTitleTask.getText(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton(R.string.dialog_cancel_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(requireContext(), "Пользователь нажал отмену", Toast.LENGTH_SHORT).show();
                    }
                })
                .setCancelable(false)
                .create();
        inputTitle.show();
    }

    public interface OnTaskSelected {
        void onTaskSelected(Task task);
    }
}