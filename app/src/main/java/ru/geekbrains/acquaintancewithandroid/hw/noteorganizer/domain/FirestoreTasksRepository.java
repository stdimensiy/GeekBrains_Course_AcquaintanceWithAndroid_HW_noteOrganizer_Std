package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

import android.content.Context;
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

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.R;

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
                        addedTask.setUpdateDate(document.getDate(FIELD_UPDATEDATE));
                        addedTask.setAlarmDate(document.getDate(FIELD_ALARMDATE));
                        addedTask.setDeadlineDate(document.getDate(FIELD_DEADLINEDATE));
                        addedTask.setElemType(document.getString(FIELD_TYPE));
                        addedTask.setElemView(document.getString(FIELD_VIEW));
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
    public void addTask(Context context, String newTitleTask, CallBack<Task> taskCallBack) {
        Map<String, Object> docData = new HashMap<>();
        Date baseCreateDate = new Date();
        if (newTitleTask.equals(""))
            newTitleTask = context.getResources().getString(R.string.default_task_title);
        docData.put(FIELD_TITLE, newTitleTask);
        docData.put(FIELD_CONTENT, context.getResources().getString(R.string.default_task_content));
        docData.put(FIELD_TYPE, context.getResources().getString(R.string.default_task_type));
        docData.put(FIELD_CREATEDATE, new Timestamp(baseCreateDate));
        docData.put(FIELD_UPDATEDATE, new Timestamp(baseCreateDate));
        docData.put(FIELD_ALARMDATE, new Timestamp(baseCreateDate));
        docData.put(FIELD_DEADLINEDATE, new Timestamp(baseCreateDate));
        docData.put(FIELD_VIEW, context.getResources().getString(R.string.default_task_view));
        String finalNewTitleTask = newTitleTask;
        db.collection(COLLECTION).add(docData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.w(TAG, "Дефолтная заадача добавлена в базу данных");
                Task task = new Task(finalNewTitleTask, context.getResources().getString(R.string.default_task_content));
                task.setId(documentReference.getId());
                task.setCreateDate(baseCreateDate);
                task.setUpdateDate(baseCreateDate);
                task.setAlarmDate(baseCreateDate);
                task.setDeadlineDate(baseCreateDate);
                task.setElemType(context.getResources().getString(R.string.default_task_type));
                task.setElemView(context.getResources().getString(R.string.default_task_view));
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
        docData.put(FIELD_TYPE, task.getElemType());
        //docData.put(FIELD_CREATEDATE, new Timestamp(new Date())); // Дату создания сознательно не обносляем
        docData.put(FIELD_UPDATEDATE, new Timestamp(new Date()));
        docData.put(FIELD_ALARMDATE, task.getAlarmDate());
        docData.put(FIELD_DEADLINEDATE, task.getDeadlineDate());
        docData.put(FIELD_VIEW, task.getElemView());
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
