package com.example.myapplication;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
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

import com.example.myapplication.R;
import com.example.myapplication.ReminderDatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateReminderActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription, editTextDosage;
    private TextView textViewStartDateValue, textViewEndDateValue;
    private Spinner spinnerFrequency;
    private LinearLayout linearLayoutTimes; // 新增一个LinearLayout来存放时间列表

    // Database helper to store reminders
    private ReminderDatabaseHelper dbHelper;

    // 用于保存选择的所有时间
    private ArrayList<String> reminderTimes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_reminder);

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDosage = findViewById(R.id.editTextDosage);
        textViewStartDateValue = findViewById(R.id.textViewStartDateValue);
        textViewEndDateValue = findViewById(R.id.textViewEndDateValue);
        spinnerFrequency = findViewById(R.id.spinnerFrequency);
        Button buttonCreateReminder = findViewById(R.id.buttonCreateReminder);
        Button buttonAddTime = findViewById(R.id.buttonAddTime);  // 添加按钮用于新增时间
        linearLayoutTimes = findViewById(R.id.linearLayoutTimes); // 用于放置所有选定的时间

        // Initialize database helper
        dbHelper = new ReminderDatabaseHelper(this);

        // 启用 ActionBar，并显示左上角的回退按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Create Reminder");  // 设置标题
        }

        // Setup frequency Spinner选项
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.frequency_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrequency.setAdapter(adapter);

        // Set up Start Date Picker
        textViewStartDateValue.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    CreateReminderActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) ->
                            textViewStartDateValue.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1),
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Set up End Date Picker
        textViewEndDateValue.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    CreateReminderActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) ->
                            textViewEndDateValue.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1),
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Add Time Button, click to add a new time picker
        buttonAddTime.setOnClickListener(v -> {
            // 创建一个新的LinearLayout容器
            LinearLayout timeLayout = new LinearLayout(CreateReminderActivity.this);
            timeLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            timeLayout.setLayoutParams(params);

            // 创建新的TextView用于选择时间
            final TextView newTimeTextView = new TextView(CreateReminderActivity.this);
            newTimeTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            ));
            newTimeTextView.setPadding(16, 16, 16, 16);
            newTimeTextView.setBackgroundResource(R.drawable.edit_text_border); // 保持外观一致
            newTimeTextView.setText("Select time");
            newTimeTextView.setTextSize(16);
            newTimeTextView.setTextColor(getResources().getColor(android.R.color.black));

            // 创建加号按钮
            Button buttonAddTimeAgain = new Button(CreateReminderActivity.this);
            buttonAddTimeAgain.setText("+");
            buttonAddTimeAgain.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            buttonAddTimeAgain.setTextColor(getResources().getColor(android.R.color.holo_green_light));

            // 创建减号按钮
            Button buttonRemoveTime = new Button(CreateReminderActivity.this);
            buttonRemoveTime.setText("-");
            buttonRemoveTime.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            buttonRemoveTime.setTextColor(getResources().getColor(android.R.color.holo_red_light));

            // 点击减号后移除当前行
            buttonRemoveTime.setOnClickListener(v1 -> linearLayoutTimes.removeView(timeLayout));

            // 点击加号后再添加一行时间选择
            buttonAddTimeAgain.setOnClickListener(v1 -> {
                // 复制当前时间行并添加到父布局
                LinearLayout newTimeLayout = new LinearLayout(CreateReminderActivity.this);
                newTimeLayout.setOrientation(LinearLayout.HORIZONTAL);
                newTimeLayout.setLayoutParams(params);

                TextView newTimeTextViewCopy = new TextView(CreateReminderActivity.this);
                newTimeTextViewCopy.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
                ));
                newTimeTextViewCopy.setText("Select time");
                newTimeTextViewCopy.setTextSize(16);
                newTimeTextViewCopy.setTextColor(getResources().getColor(android.R.color.black));

                // 将复制的时间TextView添加到新行
                newTimeLayout.addView(newTimeTextViewCopy);
                newTimeLayout.addView(buttonRemoveTime);
                linearLayoutTimes.addView(newTimeLayout);
            });

            // 点击后显示时间选择器
            newTimeTextView.setOnClickListener(view -> {
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
            });

            // 把新的TextView和加号按钮、减号按钮加到布局中
            timeLayout.addView(newTimeTextView);
            timeLayout.addView(buttonAddTimeAgain);
            timeLayout.addView(buttonRemoveTime);
            linearLayoutTimes.addView(timeLayout);
        });

        // Handle Create Reminder button click
        buttonCreateReminder.setOnClickListener(v -> createReminder());
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
