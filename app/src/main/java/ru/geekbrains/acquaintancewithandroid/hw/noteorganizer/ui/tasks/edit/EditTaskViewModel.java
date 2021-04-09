package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.tasks.edit;

import android.text.Editable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.CallBack;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Task;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.TasksRepository;

public class EditTaskViewModel extends ViewModel {
    private final TasksRepository repository;
    private final MutableLiveData<Boolean> progressLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> saveEnabledLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<Object> saveSucceedLiveData = new MutableLiveData<>();

    public EditTaskViewModel(TasksRepository repository) {
        this.repository = repository;
    }

    // логика валидации вводимых данных
    // 1 вариант - просто непустая строка
    // 2 вариант (в разработке) проверить, если данные не изменены кнопку не активировать.
    public void validateInput(String newTitle) {
        saveEnabledLiveData.setValue(!newTitle.isEmpty()); // первая проверка, если заголовок задачи не пустой
    }

    //Логика сохранения измененных данных
    public void saveNote(Editable text, Editable content, Task task) {
        task.setTitle(text.toString());   // передаем как есть из поля редактирования в элемент
        task.setContent(content.toString()); // забираем данные контента из поля пока как есть
        //СТАРТ показа прогресс-бара
        progressLiveData.setValue(true);
        repository.updateTask(task, new CallBack<Object>() {
            @Override
            public void onResult(Object value) {
                //СТОП показа прогресс-бара
                progressLiveData.setValue(false);
                saveSucceedLiveData.setValue(new Object());
            }
        });
    }
}
