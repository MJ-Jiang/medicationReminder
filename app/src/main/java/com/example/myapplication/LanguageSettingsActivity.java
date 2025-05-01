package com.example.myapplication;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class LanguageSettingsActivity extends AppCompatActivity {
    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        getResources().getConfiguration().setLocale(locale);

        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putString("language", langCode);
        editor.apply();

        // Reload app
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);

        Button buttonEnglish = findViewById(R.id.buttonEnglish);
        Button buttonFinnish = findViewById(R.id.buttonFinnish);


        buttonEnglish.setOnClickListener(v -> setLocale("en"));
        buttonFinnish.setOnClickListener(v -> setLocale("fi"));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.settings_language);
        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // 响应返回按钮点击
        return true;
    }
}
