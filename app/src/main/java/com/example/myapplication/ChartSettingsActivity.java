package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;



public class ChartSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        if (getSupportActionBar() != null) {//先检查一下，当前界面有没有 ActionBar（标题栏）
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//在标题栏上，显示一个返回按钮（一般是左上角的 ← 小箭头）
            getSupportActionBar().setTitle(R.string.setting_chart);  // 设置标题
        }
        // Initialize views

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
