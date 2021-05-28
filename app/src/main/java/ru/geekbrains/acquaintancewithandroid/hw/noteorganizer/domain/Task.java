package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

import java.util.Calendar;

public class Task extends Note {
    private Calendar alarmDate;        //дата и время напоминания

    public Calendar getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(Calendar alarmDate) {
        this.alarmDate = alarmDate;
    }
}
