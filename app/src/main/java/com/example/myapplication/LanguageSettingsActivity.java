package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

/**
 * Activity that allows users to change the application's language settings.
 * <p>
 * Users can select from supported languages (e.g., English and Finnish), which
 * will update the app's locale, store the choice in {@link SharedPreferences},
 * and restart the main activity to apply the change.
 */

public class LanguageSettingsActivity extends AppCompatActivity {

    /**
     * Sets the app's locale based on the provided language code.
     * <p>
     * This method updates the locale, saves the setting in SharedPreferences,
     * and restarts the {@link MainActivity} to apply the language change.
     *
     * @param langCode The ISO language code to switch to (e.g., "en" or "fi").
     */
    private void setLocale(String langCode) {
        Locale locale =new Locale(langCode);//A Locale object represents a specific geographical, political, or cultural region
        Locale.setDefault(locale);//Sets this new locale as the default for the app.
        getResources().getConfiguration().setLocale(locale);//updating the language used in app resources (like strings).

        SharedPreferences.Editor editor =getSharedPreferences("settings",MODE_PRIVATE).edit();//Accesses the shared preferences file named "settings", in private mode (accessible only by this app).
        editor.putString("language", langCode);//Stores the selected language code into shared preferences so it can be retrieved later
        editor.apply();

        // Reload app
        Intent intent =new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_NEW_TASK);
        //FLAG_ACTIVITY_CLEAR_TOP: If MainActivity is already running, all other activities on top of it are closed.
        startActivity(intent);
        finish();
    }

    /**
     * Initializes the activity, sets up language selection buttons,
     * and configures the action bar.
     *
     * @param savedInstanceState The previously saved state of the activity, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);

        Button buttonEnglish =findViewById(R.id.buttonEnglish);
        Button buttonFinnish = findViewById(R.id.buttonFinnish);


        buttonEnglish.setOnClickListener(v->setLocale("en"));
        buttonFinnish.setOnClickListener(v->setLocale("fi"));
        if (getSupportActionBar() !=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Adds a back arrow.
            getSupportActionBar().setDisplayShowHomeEnabled(true);//how the app icon.
            getSupportActionBar().setTitle(R.string.settings_language);//Sets the title.
        }

    }

    /**
     * Handles the action bar's "up" button press by navigating back.
     *
     * @return true to indicate the up navigation was handled.
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
