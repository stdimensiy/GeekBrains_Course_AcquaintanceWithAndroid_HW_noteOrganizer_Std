package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain;

import android.content.Context;
import android.widget.Toast;

public interface Pluggable {
    static void toastPlug(Context context, String s) {
        Toast.makeText(context, "Действие для команды: \n"
                        + s + "\nв процессе разработки. Скоро все заработает!",
                Toast.LENGTH_SHORT).show();
    }
}
