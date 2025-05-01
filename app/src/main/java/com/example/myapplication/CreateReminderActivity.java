package com.example.myapplication;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

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


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateReminderActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription, editTextDosage;
    private TextView textViewStartDateValue, textViewEndDateValue;
    private Spinner spinnerFrequency;
    private LinearLayout linearLayoutTimes; // 存放时间列表
    private ReminderDatabaseHelper dbHelper; // Database helper to store reminders

    // 用于保存选择的所有时间
    private ArrayList<String> reminderTimes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) { //保存之前 Activity 的数据。
        super.onCreate(savedInstanceState);//不调用 super.onCreate，系统很多内建机制（比如 UI 加载、生命周期管理）就不会正确运作
        setContentView(R.layout.create_reminder);// 把界面画面加载出来

        initViews();

        dbHelper = new ReminderDatabaseHelper(this);//this 就是指当前界面的环境，让 ReminderDatabaseHelper 知道该怎么正确地打开数据库。
        setupActionBar();
        setupFrequencySpinner();
        setupDatePickers();
        initTimeRows();
        setupCreateButton();
    }
    private void initViews(){
        editTextName = findViewById(R.id.editTextName);//在当前界面(layout)里，找到这个具体的小组件(view)
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDosage = findViewById(R.id.editTextDosage);
        textViewStartDateValue = findViewById(R.id.textViewStartDateValue);
        textViewEndDateValue = findViewById(R.id.textViewEndDateValue);
        spinnerFrequency = findViewById(R.id.spinnerFrequency);
        linearLayoutTimes = findViewById(R.id.linearLayoutTimes); // 用于放置所有选定的时间
    }
        private void setupActionBar(){
            // 启用 ActionBar，并显示左上角的回退按钮
            if (getSupportActionBar() != null) {//先检查一下，当前界面有没有 ActionBar（标题栏）
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);//在标题栏上，显示一个返回按钮（一般是左上角的 ← 小箭头）
                getSupportActionBar().setTitle(R.string.create_reminder);  // 设置标题
            }
        }
       private void setupFrequencySpinner(){
           // Setup frequency Spinner选项
           ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                   this, R.array.frequency_array, android.R.layout.simple_spinner_item);
           //ArrayAdapter<CharSequence> 是一个适配器，把数组的数据（比如频率选项）放到 Spinner（下拉框）里。
           //createFromResource() 是一个工厂方法，直接从 XML 文件中创建一个 ArrayAdapter。
           //android.R.layout.simple_spinner_item：系统自带的简单布局样式，表示每一项的长相。
           adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//设置下拉列表打开时每个选项的布局样式
           spinnerFrequency.setAdapter(adapter);//把准备好的数据和 Spinner 连接起来。
       }
       private void setupDatePickers(){
           // Set up Start Date Picker
           textViewStartDateValue.setOnClickListener(v -> showDatePickerDialog(textViewStartDateValue));
           // Set up End Date Picker
           textViewEndDateValue.setOnClickListener(v -> showDatePickerDialog(textViewEndDateValue));
       }
       private void initTimeRows(){
        addTimeRow(true);
       }
       private void setupCreateButton(){
           Button buttonCreateReminder = findViewById(R.id.buttonCreateReminder);
          buttonCreateReminder.setOnClickListener(v->createReminder());

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


    private void addTimeRow(boolean isFirstRow){
        LayoutInflater inflater = LayoutInflater.from(this);//用当前 Activity 的上下文（this）来创建一个 LayoutInflater。
        //LayoutInflater 是一个 Android 类，可以把 XML 布局文件变成 Java 中的 View 对象。
        View timeRow = inflater.inflate(R.layout.time_picker_item, linearLayoutTimes, false);
//timeRow 未来是要放到 linearLayoutTimes 里的（但现在还没加进去，因为第三个参数是 false）
        TextView timeTextView = timeRow.findViewById(R.id.textViewTime);
        Button buttonAdd = timeRow.findViewById(R.id.buttonAddTime);
        Button buttonRemove = timeRow.findViewById(R.id.buttonRemoveTime);
        timeTextView.setOnClickListener(v -> showTimePickerDialog(timeTextView));

        // 加号按钮点击事件（继续新增一行）
        buttonAdd.setOnClickListener(v -> addTimeRow(false));
        if(isFirstRow){
            buttonRemove.setVisibility(View.INVISIBLE);
        }else {
            buttonRemove.setVisibility(View.VISIBLE);
            buttonRemove.setOnClickListener(v -> {
                if (linearLayoutTimes.getChildCount() > 1) {
                    // 从时间列表中移除对应时间
                    String timeToRemove = timeTextView.getText().toString();
                    if (!timeToRemove.equals("Select Time")) {
                        reminderTimes.remove(timeToRemove);
                    }
                    linearLayoutTimes.removeView(timeRow);
                }
            });
        }
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
        // 1. 验证输入
        if (!validateInput()) return;

        // 2. 准备数据对象
        Reminder reminder = prepareReminderData();

        // 3. 存储到数据库
        long result = dbHelper.addReminder(reminder);

        // 4. 处理结果
        if (result != -1) {
            showSuccessDialog();
            clearFields();
        } else {
            Toast.makeText(this, getString(R.string.fail_save_reminder), Toast.LENGTH_SHORT).show();

        }
    }

    private boolean validateInput() {
        // 检查基础字段
        if (editTextName.getText().toString().trim().isEmpty() ||
                editTextDescription.getText().toString().trim().isEmpty() ||
                editTextDosage.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.fill_all_fields));
            return false;
        }

        // 检查日期
        if (textViewStartDateValue.getText().toString().equals("Select start date") ||
                textViewEndDateValue.getText().toString().equals("Select end date")) {
            showToast(getString(R.string.select_dates));
            return false;
        }

        // 检查至少有一个有效时间
        if (!hasValidTimeSelected()) {
            showToast(getString(R.string.select_time));
            return false;
        }

        return true;
    }

    private boolean hasValidTimeSelected() {
        for (int i = 0; i < linearLayoutTimes.getChildCount(); i++) {
            View row = linearLayoutTimes.getChildAt(i);
            TextView timeText = row.findViewById(R.id.textViewTime);
            if (!timeText.getText().toString().equals("Select Time")) {
                return true;
            }
        }
        return false;
    }

    private Reminder prepareReminderData() {
        Reminder reminder = new Reminder();

        // 基础信息
        reminder.setName(editTextName.getText().toString().trim());
        reminder.setDescription(editTextDescription.getText().toString().trim());
        reminder.setDosage(editTextDosage.getText().toString().trim());
        reminder.setFrequency(spinnerFrequency.getSelectedItem().toString());

        // 日期处理
        String displayStart = textViewStartDateValue.getText().toString();
        String displayEnd = textViewEndDateValue.getText().toString();
        reminder.setDisplayStartDate(displayStart);
        reminder.setDisplayEndDate(displayEnd);
        reminder.setStartDate(convertToDatabaseDate(displayStart));
        reminder.setEndDate(convertToDatabaseDate(displayEnd));
        reminder.setIsCompleted(false);
        // 收集所有有效时间
        ArrayList<String> times = new ArrayList<>();
        for (int i = 0; i < linearLayoutTimes.getChildCount(); i++) {
            View row = linearLayoutTimes.getChildAt(i);
            TextView timeText = row.findViewById(R.id.textViewTime);
            String time = timeText.getText().toString();
            if (!time.equals("Select Time")) {
                times.add(time);
            }
        }
        reminder.setTimes(times);

        return reminder;
    }

    private String convertToDatabaseDate(String displayDate) {
        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inFormat.parse(displayDate);
            return outFormat.format(date);
        } catch (Exception e) {
            return displayDate; // 转换失败返回原值
        }
    }

    private void clearFields() {
        // 清空输入字段
        editTextName.setText("");
        editTextDescription.setText("");
        editTextDosage.setText("");
        textViewStartDateValue.setText(getString(R.string.description_startdate));
        textViewEndDateValue.setText(getString(R.string.description_enddate));
        spinnerFrequency.setSelection(0);

        // 重置时间行（保留首行）
        linearLayoutTimes.removeAllViews();
        initTimeRows();
    }


    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.success))
                .setMessage(getString(R.string.reminder_success))
                .setPositiveButton(getString(R.string.ok), (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
