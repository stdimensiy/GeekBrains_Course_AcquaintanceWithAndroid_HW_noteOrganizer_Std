package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

import java.util.Calendar;

public class Task extends Note {
    private long alarmDate;        // дата и время напоминания
    private boolean isChecked;     // состояние флажка задачи (отмечено или нет)
    private boolean isHaveContent; // признак наличия в задаче не только наименования но и содержания.

    public Task(){

    }

    public Task(String title, String content, boolean isChecked, boolean isHaveContent) {
        super(title, content);
        this.isChecked = isChecked;
        this.isHaveContent = isHaveContent;
    }

    public long getAlarmDate() {
        return alarmDate;
    }
    public void setAlarmDate(long alarmDate) {
        this.alarmDate = alarmDate;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isHaveContent() {
        return isHaveContent;
    }

    public void setHaveContent(boolean haveContent) {
        isHaveContent = haveContent;
    }
}
