package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Task;

public class TasksFragment extends Fragment {

    private TasksViewModel tasksViewModel;
    private TasksAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel = new ViewModelProvider(this, new TasksViewModelFactory()).get(TasksViewModel.class);
        tasksViewModel.fetchTasks();
        adapter = new TasksAdapter();
        adapter.setTaskClicked(new TasksAdapter.OnTaskClicked() {
            @Override
            public void onTaskClicked(Task task) {
                Toast.makeText(requireContext(), task.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Плавающая кнопка добавления новой ЗАДАЧИ нажата!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }
}