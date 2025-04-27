package com.example.myapplication;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.Calendar;

public class CreateReminderActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription, editTextDosage;
    private TextView textViewStartDateValue, textViewEndDateValue;
    private Spinner spinnerFrequency;
    private LinearLayout linearLayoutTimes; // 新增一个LinearLayout来存放时间列表
    private ReminderDatabaseHelper dbHelper; // Database helper to store reminders

    // 用于保存选择的所有时间
    private ArrayList<String> reminderTimes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) { //保存之前 Activity 的数据。
        super.onCreate(savedInstanceState);//不调用 super.onCreate，系统很多内建机制（比如 UI 加载、生命周期管理）就不会正确运作！
        setContentView(R.layout.create_reminder);// 把界面画面加载出来

        // Initialize views
        editTextName = findViewById(R.id.editTextName);//在当前界面(layout)里，找到这个具体的小组件(view)
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDosage = findViewById(R.id.editTextDosage);
        textViewStartDateValue = findViewById(R.id.textViewStartDateValue);
        textViewEndDateValue = findViewById(R.id.textViewEndDateValue);
        spinnerFrequency = findViewById(R.id.spinnerFrequency);
        Button buttonCreateReminder = findViewById(R.id.buttonCreateReminder);
        Button buttonAddTime = findViewById(R.id.buttonAddTime);  // 添加按钮用于新增时间
        linearLayoutTimes = findViewById(R.id.linearLayoutTimes); // 用于放置所有选定的时间

        // Initialize database helper
        dbHelper = new ReminderDatabaseHelper(this);//this 就是指当前界面的环境，让 ReminderDatabaseHelper 知道该怎么正确地打开数据库。

        buttonCreateReminder.setOnClickListener(v -> createReminder());
        // 启用 ActionBar，并显示左上角的回退按钮
        if (getSupportActionBar() != null) {//先检查一下，当前界面有没有 ActionBar（标题栏）
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//在标题栏上，显示一个返回按钮（一般是左上角的 ← 小箭头）
            getSupportActionBar().setTitle("Create Reminder");  // 设置标题
        }

        // Setup frequency Spinner选项
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.frequency_array, android.R.layout.simple_spinner_item);
        //ArrayAdapter<CharSequence> 是一个适配器，把数组的数据（比如频率选项）放到 Spinner（下拉框）里。
        //createFromResource() 是一个工厂方法，直接从 XML 文件中创建一个 ArrayAdapter。
        //android.R.layout.simple_spinner_item：系统自带的简单布局样式，表示每一项的长相。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//设置下拉列表打开时每个选项的布局样式
        spinnerFrequency.setAdapter(adapter);//把准备好的数据和 Spinner 连接起来。

        // Set up Start Date Picker
        textViewStartDateValue.setOnClickListener(v -> showDatePickerDialog(textViewStartDateValue));
        // Set up End Date Picker
        textViewEndDateValue.setOnClickListener(v -> showDatePickerDialog(textViewEndDateValue));

        buttonAddTime.setOnClickListener(v -> addTimeRow());

        // Handle Create Reminder button click
        buttonCreateReminder.setOnClickListener(v -> createReminder());
    }
    private void showDatePickerDialog(TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                CreateReminderActivity.this,
                (view, year1, monthOfYear, dayOfMonth) ->
                        textView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1),////当用户选完日期后要执行的逻辑
                year, month, day////默认初始选中的日期
        );////month要+1，因为 Java 的月份从0开始计数。
        datePickerDialog.show();
    }

    private void addTimeRow() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View timeRow = inflater.inflate(R.layout.time_picker_item, linearLayoutTimes, false);

        TextView timeTextView = timeRow.findViewById(R.id.textViewTime);
        Button buttonAdd = timeRow.findViewById(R.id.buttonAddTime);
        Button buttonRemove = timeRow.findViewById(R.id.buttonRemoveTime);

        // 新增的行显示减号按钮
        buttonRemove.setVisibility(View.VISIBLE);

        // 设置时间选择点击事件
        timeTextView.setOnClickListener(v -> showTimePickerDialog(timeTextView));

        // 加号按钮点击事件（继续新增一行）
        buttonAdd.setOnClickListener(v -> addTimeRow());

        // 减号按钮点击事件
        buttonRemove.setOnClickListener(v -> {
            if (linearLayoutTimes.getChildCount() > 0) {
                linearLayoutTimes.removeView(timeRow);
            }
        });

        // 添加到布局
        linearLayoutTimes.addView(timeRow);
    }

    private void showTimePickerDialog(TextView newTimeTextView) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                CreateReminderActivity.this,
                (timePicker, hourOfDay, minute1) -> {
                    String formattedTime = String.format("%02d:%02d", hourOfDay, minute1);
                    newTimeTextView.setText(formattedTime);
                    reminderTimes.add(formattedTime); // 保存该时间到列表中
                },
                hour, minute, true
        );
        timePickerDialog.show();
    }

    private void createReminder() {
        String reminderName = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String dosage = editTextDosage.getText().toString().trim();
        String startDate = textViewStartDateValue.getText().toString().trim();
        String endDate = textViewEndDateValue.getText().toString().trim();
        String frequency = spinnerFrequency.getSelectedItem().toString().trim();

        // 检查用户是否填写了必要信息
        if (reminderName.isEmpty() || description.isEmpty() || dosage.isEmpty() ||
                startDate.equals("Select start date") || endDate.equals("Select end date") ||
                reminderTimes.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields and select at least one time.", Toast.LENGTH_SHORT).show();
        } else {
            // Store the reminder data in the database, including all times
            dbHelper.addReminder(reminderName, description, dosage, startDate, endDate, frequency, reminderTimes);

            // Show success dialog
            showSuccessDialog();

            // Clear fields after creating the reminder
            clearFields();
        }
    }

    private void clearFields() {
        editTextName.setText("");
        editTextDescription.setText("");
        editTextDosage.setText("");
        textViewStartDateValue.setText("Select start date");
        textViewEndDateValue.setText("Select end date");
        spinnerFrequency.setSelection(0); // Reset spinner to default
        reminderTimes.clear();  // 清空时间列表
        linearLayoutTimes.removeAllViews(); // 清空所有已添加的时间
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success")
                .setMessage("You've created the reminder successfully.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();  // Return to the previous page (reminder list page)
        return true;
    }
}
