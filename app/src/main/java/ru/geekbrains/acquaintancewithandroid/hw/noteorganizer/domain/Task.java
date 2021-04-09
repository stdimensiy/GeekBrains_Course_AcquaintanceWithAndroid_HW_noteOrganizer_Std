package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

import java.util.Date;

public class Task extends Note {
    // поля объявленные в базовом классе Note
//    public String id;                   //уникальный идентификатор записи
//    private String title;               //заголовок заметки
//    private String content;             //содержимое заметки
//    private Date createDate;            //дата и время создания заметки
//    private Date updateDate;            //дата и время последнего редактирования заметки
//    private boolean marked;             //флаг состояния заметки (отмечена или не отмечена)

    private Date alarmDate;        // дата и время напоминания
    private boolean isChecked;     // состояние флажка задачи (отмечено или нет)

    public Task() {

    }

    public Task(String title, String content) {
        super(title, content);
        //значения по умолчанию
        setCreateDate(new Date());
        setUpdateDate(new Date());
        this.isChecked = false;
    }

    public Date getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(Date alarmDate) {
        this.alarmDate = alarmDate;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
