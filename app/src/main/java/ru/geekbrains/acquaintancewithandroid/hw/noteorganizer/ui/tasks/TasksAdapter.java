package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.R;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Task;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {
    private ArrayList<Task> items = new ArrayList<>();
    private OnTaskClicked taskClicked;

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

    public void addItems(ArrayList<Task> toAdd) {
        items.addAll(toAdd);
    }

    public void clear() {
        items.clear();
    }

    interface OnTaskClicked {
        void onTaskClicked(Task task);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTask;
        private ImageButton buttonTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTask = itemView.findViewById(R.id.textView);
            buttonTask = itemView.findViewById(R.id.imageButton);
            buttonTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (taskClicked != null) {
                        taskClicked.onTaskClicked(items.get(getAdapterPosition()));
                    }
                }
            });
        }

        public TextView getTitleTask() {
            return titleTask;
        }
    }

}
