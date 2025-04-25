package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class CreateReminderActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription, editTextDosage;
    private TextView textViewStartDateValue, textViewEndDateValue, textViewTime;
    private Spinner spinnerFrequency;

    // Database helper to store reminders
    private ReminderDatabaseHelper dbHelper;

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
        textViewTime = findViewById(R.id.textViewTime);
        spinnerFrequency = findViewById(R.id.spinnerFrequency);
        Button buttonCreateReminder = findViewById(R.id.buttonCreateReminder);

        // Initialize database helper
        dbHelper = new ReminderDatabaseHelper(this);

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

        // Set up Time Picker for Reminder Time
        textViewTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    CreateReminderActivity.this,
                    (view, hourOfDay, minute1) ->
                            textViewTime.setText(String.format("%02d:%02d", hourOfDay, minute1)),
                    hour, minute, true
            );
            timePickerDialog.show();
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
        String frequency = spinnerFrequency.getSelectedItem().toString();
        String reminderTime = textViewTime.getText().toString().trim();

        if (reminderName.isEmpty() || description.isEmpty() || dosage.isEmpty() ||
                startDate.equals("Select start date") || endDate.equals("Select end date") ||
                reminderTime.equals("Select Time")) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        } else {
            // Store the reminder data in the database
            dbHelper.addReminder(reminderName, description, dosage, startDate, endDate, frequency, reminderTime);

            // Show success message
            Toast.makeText(this, "You've created the reminder successfully.", Toast.LENGTH_SHORT).show();

            // Optional: Clear fields after creating the reminder
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
        textViewTime.setText("Select Time");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();  // Return to the previous page (reminder list page)
        return true;
    }
}
