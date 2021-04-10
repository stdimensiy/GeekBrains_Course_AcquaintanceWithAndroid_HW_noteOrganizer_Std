package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.R;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Pluggable;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.settings_options_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // обработка нажатия конкретных пунктов меню.
            // позиционирование происходит по идентификатору (наименование игнорируется и может быть
            // любым и на любом языке)
            // новые пункты именю или идентификаторы обработчики к ктороым не реализованы игнорируются.
            case R.id.action_exit:
                Pluggable.toastPlug(requireContext(), "Закрыть приложение");
                break;
            case R.id.action_help:
                Pluggable.toastPlug(requireContext(), "Инструкция для настроек");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}