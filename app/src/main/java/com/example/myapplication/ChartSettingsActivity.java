package com.example.myapplication;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ChartSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.setting_chart);
        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // 响应返回按钮点击
        return true;
    }
}
