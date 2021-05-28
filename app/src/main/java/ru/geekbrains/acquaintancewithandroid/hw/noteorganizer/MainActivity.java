package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Note;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Task;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.notes.NotesFragment;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.notes.edit.EditNoteFragment;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.tasks.TasksFragment;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.tasks.edit.EditTaskFragment;

public class MainActivity extends AppCompatActivity implements NotesFragment.OnNoteSelected, EditNoteFragment.OnNoteSaved, TasksFragment.OnTaskSelected, EditTaskFragment.OnTaskSaved {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // для бокового меню
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_notes, R.id.navigation_tasks, R.id.navigation_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //Для нижнего меню
        BottomNavigationView navView = findViewById(R.id.nav_view_bottom);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_options_menu, menu);
        return true;
    }

    // фрагмент отвечает за поведение верхнего навигационного меню и конкретно гамбургера
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onNoteSelected(Note note) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putParcelable("ARG_NOTE", note);
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.EditNoteFragment, bundle);
        //Toast.makeText(this, fragmentManager.getFragments().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoteSaved() {
        Navigation.findNavController(this, R.id.nav_host_fragment).popBackStack();
    }

    @Override
    public void onTaskSelected(Task task) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putParcelable("ARG_TASK", task);
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.editTaskFragment, bundle);
        //Toast.makeText(this, fragmentManager.getFragments().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskSaved() {
        Navigation.findNavController(this, R.id.nav_host_fragment).popBackStack();
    }

}