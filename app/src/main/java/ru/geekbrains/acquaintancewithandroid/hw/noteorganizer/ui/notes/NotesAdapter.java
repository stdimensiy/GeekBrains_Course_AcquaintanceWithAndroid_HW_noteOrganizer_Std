package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.notes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.R;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private final Fragment fragment;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("E, d MMM yyyy \n (HH:mm)");
    private ArrayList<Note> items = new ArrayList<>();
    private OnNoteClicked noteClicked;
    private OnNoteLongClicked noteLongClicked;

    public NotesAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public OnNoteLongClicked getNoteLongClicked() {
        return noteLongClicked;
    }

    public void setNoteLongClicked(OnNoteLongClicked noteLongClicked) {
        this.noteLongClicked = noteLongClicked;
    }

    public OnNoteClicked getNoteClicked() {
        return noteClicked;
    }

    public void setNoteClicked(OnNoteClicked noteClicked) {
        this.noteClicked = noteClicked;
    }

    public void addItems(ArrayList<Note> toAdd) {
        items.addAll(toAdd);
    }

    public void clear() {
        items.clear();
    }

    // Временный метод объединяющммий функционал методов addItems и clear (улучшение для экономии места)
    // TODO после тестирования приняять решение и убрать лишние методы
    public void setItems(ArrayList<Note> toSet) {
        items.clear();
        items.addAll(toSet);
    }

    //метод добавления нового элемента в коллекцию
    public void addItem(Note note) {
        items.add(note);
    }

    public void deleteItem(int position) {
        items.remove(position);
    }

    @NonNull
    @Override
    public NotesAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NoteViewHolder holder, int position) {
        Note item = items.get(position);
        holder.getTitleNote().setText(item.getTitle());
        holder.getContentNote().setText(item.getContent());
        //dateFormat.format(item.getCreateDate())
        holder.getDateNote().setText(dateFormat.format(item.getCreateDate()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Note getItemAtIndex(int contextMenuItemPosition) {
        return items.get(contextMenuItemPosition);
    }

    interface OnNoteClicked {
        void onNoteClicked(Note note);
    }

    interface OnNoteLongClicked {
        void onNoteLongClicked(View itemView, int position, Note note);
    }


    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleNote;
        private final TextView contentNote;
        private final TextView dateNote;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleNote = itemView.findViewById(R.id.textView);
            contentNote = itemView.findViewById(R.id.textView_contentNote);
            dateNote = itemView.findViewById(R.id.textView_dateNote);
            fragment.registerForContextMenu(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (noteClicked != null) {
                        noteClicked.onNoteClicked(items.get(getAdapterPosition()));
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (noteLongClicked != null) {
                        noteLongClicked.onNoteLongClicked(itemView, getAdapterPosition(), items.get(getAdapterPosition()));
                    }
                    return false;
                }
            });
        }
        public TextView getTitleNote() {
            return titleNote;
        }

        public TextView getContentNote() {
            return contentNote;
        }

        public TextView getDateNote() {
            return dateNote;
        }
    }
}
