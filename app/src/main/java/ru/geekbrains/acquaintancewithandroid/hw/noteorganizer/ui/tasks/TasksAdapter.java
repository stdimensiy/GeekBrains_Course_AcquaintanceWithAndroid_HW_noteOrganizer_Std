package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.tasks;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.R;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Task;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {
    private ArrayList<Task> items = new ArrayList<>();
    private OnTaskClicked taskClicked;
    private final Fragment fragment;

    public TasksAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public TasksAdapter.OnTaskLongClicked getTaskLongClicked() {
        return taskLongClicked;
    }

    public void setTaskLongClicked(TasksAdapter.OnTaskLongClicked taskLongClicked) {
        this.taskLongClicked = taskLongClicked;
    }

    private TasksAdapter.OnTaskLongClicked taskLongClicked;

    public OnTaskClicked getTaskClicked() {
        return taskClicked;
    }

    public void setTaskClicked(OnTaskClicked taskClicked) {
        this.taskClicked = taskClicked;
    }

    @NonNull
    @Override
    public TasksAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksAdapter.TaskViewHolder holder, int position) {
        Task item = items.get(position);
        holder.getTitleTask().setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Task getItemAtIndex(int contextMenuItemPosition) {
        return items.get(contextMenuItemPosition);
    }

    public void addItems(ArrayList<Task> toAdd) {
        items.addAll(toAdd);
    }

    public void clear() {
        items.clear();
    }

    interface OnTaskClicked {
        void onTaskClicked(Task task);
    }

    interface OnTaskLongClicked {
        void onTaskLongClicked(View itemView, int position, Task task);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTask;
        private ImageButton buttonTask;
        private ImageButton buttonTaskEdit;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTask = itemView.findViewById(R.id.textView);
            buttonTask = itemView.findViewById(R.id.imageButton_ViewСontent);
            buttonTaskEdit = itemView.findViewById(R.id.imageButton_EditTask);
            buttonTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.w("TASKSADAPTER", "событие обычного клика отлавливается");
                    if (taskClicked != null) {
                        taskClicked.onTaskClicked(items.get(getAdapterPosition()));
                    }
                }
            });
            buttonTaskEdit.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.w("TASKSADAPTER", "событие долгого клика отлавливается");
                    if (taskLongClicked != null) {
                        taskLongClicked.onTaskLongClicked(itemView, getAdapterPosition(), items.get(getAdapterPosition()));
                    }
                    return false;
                }
            });
        }

        public TextView getTitleTask() {
            return titleTask;
        }
    }

}
