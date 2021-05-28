package ru.geekbrains.acquaintancewithandroid.hw.noteorganizer;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.domain.Note;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.notes.NotesFragment;
import ru.geekbrains.acquaintancewithandroid.hw.noteorganizer.ui.notes.edit.EditNoteFragment;

public class MainActivity extends AppCompatActivity implements NotesFragment.OnNoteSelected, EditNoteFragment.OnNoteSaved {

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
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_notes, R.id.navigation_tasks, R.id.navigation_settings)
                .build();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuCompat.setGroupDividerEnabled(menu, true);
        getMenuInflater().inflate(R.menu.notes_options_menu, menu);
        return true;
    }

    @Override
    public void onNoteSelected(Note note) {
       FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.nav_host_fragment, EditNoteFragment.newInstance(note), EditNoteFragment.TAG);
//        fragmentTransaction.commit();
        Fragment oldFragment = fragmentManager.findFragmentByTag(NotesFragment.TAG);
        getSupportFragmentManager()
                .beginTransaction().addToBackStack("Old")
                .replace(R.id.nav_host_fragment, EditNoteFragment.newInstance(note), EditNoteFragment.TAG)
                .addToBackStack(EditNoteFragment.TAG)
                .commit();
        //Toast.makeText(this, fragmentManager.getFragments().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoteSaved() {
        getSupportFragmentManager().popBackStack();
    }
}