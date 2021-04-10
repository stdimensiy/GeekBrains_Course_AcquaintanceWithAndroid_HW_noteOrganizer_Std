package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class FirestoreTasksRepository implements TasksRepository {
    public static final FirestoreTasksRepository INSTANCE = new FirestoreTasksRepository();
    private static final String TAG = "FireStore";
    //Структура хранилища задачи в FS
    private static final String COLLECTION = "tasks";                    // базовая коллекция задач
    //Структура данных заметки в FS
    private static final String FIELD_TITLE = "title";                   // заголовок задачи
    private static final String FIELD_CONTENT = "content";               // описание задачи
    private static final String FIELD_TYPE = "typeTask";                 // тип задачи ( прочее / важное / учеба / работа)
    private static final String FIELD_VIEW = "viewTask";                 // вид карточки задачи ( обычная (разовая) задача / циклическая / накопительная)
    private static final String FIELD_CREATEDATE = "date_create";        // дата создания задачи
    private static final String FIELD_UPDATEDATE = "date_update";        // дата редактирования задачи
    private static final String FIELD_ALARMDATE = "date_alarm";          // дата создания задачи
    private static final String FIELD_DEADLINEDATE = "date_deadline";    // дата редактирования задачи
    //конец определения структуры
    private ArrayList<Task> tasks = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void getTasks(CallBack<ArrayList<Task>> callBack) {
        db.collection(COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    tasks.clear();
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        Task addedTask = new Task(document.getString(FIELD_TITLE), document.getString(FIELD_CONTENT));
                        addedTask.setId(document.getId());
                        addedTask.setCreateDate(document.getDate(FIELD_CREATEDATE));
                        tasks.add(addedTask);
                    }
                    callBack.onResult(tasks);
                    Log.w(TAG, "отработал модуль GETTASKS чтения из базы FS репозитория. в массиве: " + tasks.size() + " - элементов");
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }

    @Override
    public void addTask(CallBack<Task> taskCallBack) {
        Map<String, Object> docData = new HashMap<>();
        docData.put(FIELD_TITLE, "Новая задача");
        docData.put(FIELD_CONTENT, "");
        docData.put(FIELD_TYPE, "other");
        docData.put(FIELD_CREATEDATE, new Timestamp(new Date()));
        docData.put(FIELD_UPDATEDATE, new Timestamp(new Date()));
        docData.put(FIELD_ALARMDATE, new Timestamp(new Date()));
        docData.put(FIELD_DEADLINEDATE, new Timestamp(new Date()));
        docData.put(FIELD_VIEW, "usual");
        db.collection(COLLECTION).add(docData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.w(TAG, "дефолтная заадача добавлена в базу данных");
                Task task = new Task("Новая задача", "");
                task.setId(documentReference.getId());
                task.setCreateDate(new Date());
                tasks.add(task);
                taskCallBack.onResult(task);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Чтото пошло не так, новая задача не добавлена.");
            }
        });

    }

    @Override
    public void deleteTask(Task task, CallBack<Task> taskCallBack) {
        db.collection(COLLECTION).document(task.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.w(TAG, "Заметка удалена!  ID: " + task.getId());
                        tasks.remove(task);
                        taskCallBack.onResult(task);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Ошибка!!! Документ ID: " + task.getId() + " НЕ удален!", e);
            }
        });
    }

    @Override
    public void updateTask(Task task, CallBack<Object> objectCallBack) {
        Log.w(TAG, " Метод updateTask начал свою работу ");
        Map<String, Object> docData = new HashMap<>();
        docData.put(FIELD_TITLE, task.getTitle());
        docData.put(FIELD_CONTENT, task.getContent());
        docData.put(FIELD_TYPE, "other");
        //docData.put(FIELD_CREATEDATE, new Timestamp(new Date())); // Дату создания сознательно не обносляем
        docData.put(FIELD_UPDATEDATE, new Timestamp(new Date()));
        docData.put(FIELD_VIEW, "usual");
        db.collection(COLLECTION).document(task.getId()).set(docData, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.w(TAG, "Отредактированная задача добавлена в хранилище");
                objectCallBack.onResult(new Object());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Чтото пошло не так, задача не обновлена.");
            }
        });
        Log.w(TAG, "Метод updateTask ЗАКОНЧИЛ свою работу ");
    }
}
