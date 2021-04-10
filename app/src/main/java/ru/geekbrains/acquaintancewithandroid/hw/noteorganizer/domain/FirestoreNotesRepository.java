package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.R;

public class FirestoreNotesRepository implements NotesRepository {
    public static final FirestoreNotesRepository INSTANCE = new FirestoreNotesRepository();
    private static final String TAG = "FireStore";
    //Структура хранилища заметки в FS
    private static final String COLLECTION = "notes";               // заголовок зам
    //Структура данных заметки в FS
    private static final String FIELD_TITLE = "title";               // заголовок заметки
    private static final String FIELD_CONTENT = "content";           // тело заметки
    private static final String FIELD_TYPE = "typeNote";             // тип заметки ( прочее / важное / учеба / работа)
    private static final String FIELD_VIEW = "viewNote";             // вид карточки заметки ( обычная запись / картинка)
    private static final String FIELD_CREATEDATE = "date_create";    // дата создания заметки
    private static final String FIELD_UPDATEDATE = "date_update";    // дата редактирования заметки
    //конец определения структуры заметки
    private final ArrayList<Note> notes = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void getNotes(CallBack<ArrayList<Note>> callBack) {
        db.collection(COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    notes.clear();
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        Note addedNote = new Note(document.getString(FIELD_TITLE), document.getString(FIELD_CONTENT));
                        addedNote.setId(document.getId());
                        addedNote.setCreateDate(document.getDate(FIELD_CREATEDATE));
                        addedNote.setUpdateDate(document.getDate(FIELD_UPDATEDATE));
                        addedNote.setElemType(document.getString(FIELD_TYPE));
                        addedNote.setElemView(document.getString(FIELD_VIEW));
                        notes.add(addedNote);
                    }
                    callBack.onResult(notes);
                    Log.w(TAG, "отработал модуль GETNOTE чтения из базы FS репозитория. в массиве: " + notes.size() + " - элементов");
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }

    @Override
    public void addNewTestNote(Context context, CallBack<Note> noteCallBack) {
        Map<String, Object> docData = new HashMap<>();
        Date baseCreateDate = new Date();
        docData.put(FIELD_TITLE, context.getResources().getString(R.string.default_note_title));
        docData.put(FIELD_CONTENT, context.getResources().getString(R.string.default_note_content));
        docData.put(FIELD_TYPE, context.getResources().getString(R.string.default_note_type));
        docData.put(FIELD_CREATEDATE, new Timestamp(baseCreateDate));
        docData.put(FIELD_UPDATEDATE, new Timestamp(baseCreateDate));
        docData.put(FIELD_VIEW, context.getResources().getString(R.string.default_note_view));
        db.collection(COLLECTION).add(docData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.w(TAG, "Пустая новая заметка добавлена в базу облако");
                Note note = new Note(context.getResources().getString(R.string.default_note_title), context.getResources().getString(R.string.default_note_content));
                note.setId(documentReference.getId());
                note.setCreateDate(baseCreateDate);
                note.setUpdateDate(note.getCreateDate());
                note.setElemType(context.getResources().getString(R.string.default_note_type));
                note.setElemView(context.getResources().getString(R.string.default_note_view));
                notes.add(note);
                noteCallBack.onResult(note);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Чтото пошло не так, заметка не добавлена.");
            }
        });
    }

    @Override
    public void deleteNote(Note note, CallBack<Note> noteCallBack) {
        db.collection(COLLECTION).document(note.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.w(TAG, "Заметка удалена!  ID: " + note.getId());
                        notes.remove(note);
                        noteCallBack.onResult(note);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Ошибка!!! Документ ID: " + note.getId() + " НЕ удален!", e);
            }
        });
    }

    @Override
    public void clearAllNotes(CallBack<Object> voidCallBack) {
        //согласно правил Firestore документы правильно удаляются последовательно из коллекции по одному
        //коллекция уничтожается автоматически когда в ней не останется элементов
        // следовательно реализация данного метода переносится в цикли организуемый NoteViewModel
        // в котором последовательно вызываются метода deleteNote
        // процесс отслеживаемый, во вьюшке анимированный.
    }

    @Override
    public void updateNote(Note note, CallBack<Object> objectCallBack) {
        Map<String, Object> docData = new HashMap<>();
        docData.put(FIELD_TITLE, note.getTitle());
        docData.put(FIELD_CONTENT, note.getContent());
        docData.put(FIELD_TYPE, note.getElemType());
        //docData.put(FIELD_CREATEDATE, new Timestamp(new Date())); // Дату создания сознательно не обносляем
        docData.put(FIELD_UPDATEDATE, new Timestamp(new Date()));
        docData.put(FIELD_VIEW, note.getElemView());
        db.collection(COLLECTION).document(note.getId()).set(docData, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.w(TAG, "Отредактированная заметка добавлена в хранилище");
                objectCallBack.onResult(new Object());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Чтото пошло не так, заметка не обновлена.");
            }
        });
    }
}
