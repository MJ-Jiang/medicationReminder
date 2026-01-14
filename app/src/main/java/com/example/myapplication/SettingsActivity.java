package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity that provides access to the application's settings.
 * <p>
 * This screen includes navigation options to sub-settings such as language preferences
 * and chart-related settings. It also enables support for the ActionBar's "up" button.
 */
public class SettingsActivity extends AppCompatActivity {
    /**
     * Initializes the settings screen, sets up the action bar, and attaches listeners
     * to the settings options (language and chart settings).
     *
     * @param savedInstanceState The previously saved instance state of the activity, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.app_settings);
        }
        LinearLayout settingLanguage = findViewById(R.id.settingLanguage);//a clickable item in settings screen that represents "Language Settings".
        settingLanguage.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, LanguageSettingsActivity.class);
            //Creates an Intent to go from the current activity (SettingsActivity) to a new activity (LanguageSettingsActivity).
            startActivity(intent);
        });

        LinearLayout settingChart = findViewById(R.id.settingChart);
        settingChart.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ChartSettingsActivity.class);
            startActivity(intent);
        });


    }

    /**
     * Handles the ActionBar's "up" button press by navigating back.
     *
     * @return true to indicate the action was handled.
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

