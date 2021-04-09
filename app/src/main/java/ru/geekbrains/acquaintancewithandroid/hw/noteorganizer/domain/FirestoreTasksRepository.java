package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FirestoreTasksRepository implements TasksRepository {
    public static final FirestoreTasksRepository INSTANCE = new FirestoreTasksRepository();
    private static final String TAG = "FireStore";
    //Структура хранилища задачи в FS
    private static final String COLLECTION = "tasks";                    // базовая коллекция задач
    //Структура данных заметки в FS
    private static final String FIELD_TITLE = "title";                   // заголовок задачи
    private static final String FIELD_CONTENT = "content";               // описание задачи
    private static final String FIELD_TYPE = "typeNote";                 // тип задачи ( прочее / важное / учеба / работа)
    private static final String FIELD_VIEW = "viewNote";                 // вид карточки задачи ( обычная (разовая) задача / циклическая / накопительная)
    private static final String FIELD_CREATEDATE = "date_create";        // дата создания задачи
    private static final String FIELD_UPDATEDATE = "date_update";        // дата редактирования задачи
    private static final String FIELD_ALARMDATE = "date_alarm";          // дата создания задачи
    private static final String FIELD_DEADLINEDATE = "date_deadline";    // дата редактирования задачи
    //конец определения структуры
    private final Executor executor = Executors.newCachedThreadPool();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private ArrayList<Task> tasks = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void getTasks(CallBack<ArrayList<Task>> callBack) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        db.collection(COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    tasks.clear();
                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                        Task addedTask = new Task(document.getString("title"), document.getString("content"));
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
                });
            }
        });
    }

    @Override
    public void addTask(String title) {

    }

    @Override
    public void deleteTask(int index) {

    }

    @Override
    public void updateTask(Task task, CallBack<Object> objectCallBack) {

    }
}
