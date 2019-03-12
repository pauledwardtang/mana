package io.phatcat.mana.view;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import dagger.android.AndroidInjection;
import dagger.android.DaggerActivity;
import dagger.android.support.DaggerAppCompatActivity;
import io.phatcat.mana.R;

public class MainActivity extends DaggerAppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setIcon(R.mipmap.ic_launcher_round);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
            actionBar.setTitle(R.string.app_name);
        }
    }
}
