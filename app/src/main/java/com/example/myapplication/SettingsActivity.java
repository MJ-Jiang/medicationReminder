package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        // 启用 ActionBar 的返回箭头
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.app_settings);
        }
        LinearLayout settingLanguage = findViewById(R.id.settingLanguage);
        settingLanguage.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, LanguageSettingsActivity.class);
            startActivity(intent);
        });

        LinearLayout settingChart = findViewById(R.id.settingChart);
        settingChart.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ChartSettingsActivity.class);
            startActivity(intent);
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // 响应返回按钮点击
        return true;
    }
}

