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
/**
 * {@code CreateReminderActivity} allows the user to create a new reminder with
 * details such as name, description, dosage, start/end dates, frequency, and multiple times.
 *
 * <p>The activity provides input validation, date and time pickers, and dynamically
 * manages multiple reminder times.</p>
 *
 * <p>Once the reminder data is validated, it is saved into the database with each time
 * saved as a separate reminder linked by a common group ID.</p>
 */
public class CreateReminderActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription, editTextDosage;
    private TextView textViewStartDateValue, textViewEndDateValue;
    private Spinner spinnerFrequency;
    private LinearLayout linearLayoutTimes; //
    private ReminderDatabaseHelper dbHelper; //
    /**
     * Initializes the UI components, database helper, action bar, spinners, date pickers,
     * and event listeners.
     *
     * @param savedInstanceState saved state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_reminder);

        initViews();

        dbHelper = new ReminderDatabaseHelper(this);
        setupActionBar();
        setupFrequencySpinner();
        setupDatePickers();
        initTimeRows();
        setupCreateButton();
    }
    /**
     * Finds and assigns all views from the layout.
     */
    private void initViews(){
        editTextName =findViewById(R.id.editTextName);
        editTextDescription =findViewById(R.id.editTextDescription);
        editTextDosage =findViewById(R.id.editTextDosage);
        textViewStartDateValue =findViewById(R.id.textViewStartDateValue);
        textViewEndDateValue =findViewById(R.id.textViewEndDateValue);
        spinnerFrequency =findViewById(R.id.spinnerFrequency);
        linearLayoutTimes =findViewById(R.id.linearLayoutTimes);
    }
    /**
     * Sets up the action bar with back navigation and title.
     */
    private void setupActionBar(){
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.create_reminder);
        }
    }
    /**
     * Initializes the frequency spinner with frequency options.
     */
    private void setupFrequencySpinner(){

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.frequency_array, android.R.layout.simple_spinner_item);//Use ArrayAdapter.createFromResource() to load data from a resource file
//<CharSequence> is a Java generic syntax, indicating that this adapter is used to manage a set of character sequences (characters or strings).
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrequency.setAdapter(adapter);//Set the adapter just created and configured to spinnerFrequency to display these frequency options
    }
    /**
     * Sets click listeners on start and end date TextViews to show date picker dialogs.
     */
    private void setupDatePickers(){
        textViewStartDateValue.setOnClickListener(v -> DatePickerHelper.showDatePickerDialog(this, textViewStartDateValue));
        textViewEndDateValue.setOnClickListener(v ->  DatePickerHelper.showDatePickerDialog(this, textViewEndDateValue));
    }
    /**
     * Initializes the first time row for picking reminder times.
     */
    private void initTimeRows(){
        addTimeRow(true);
    }
    /**
     * Sets up the create button to validate input and create a reminder on click.
     */
    private void setupCreateButton(){
        Button buttonCreateReminder = findViewById(R.id.buttonCreateReminder);
        buttonCreateReminder.setOnClickListener(v->createReminder());

    }

    /**
     * Adds a new time picker row to allow the user to select multiple reminder times.
     *
     * @param isFirstRow whether this is the first time row (controls remove button visibility)
     */
    private void addTimeRow(boolean isFirstRow){
        LayoutInflater inflater = LayoutInflater.from(this);
        View timeRow = inflater.inflate(R.layout.time_picker_item, linearLayoutTimes, false);

        TextView timeTextView = timeRow.findViewById(R.id.textViewTime);
        Button buttonAdd = timeRow.findViewById(R.id.buttonAddTime);
        Button buttonRemove = timeRow.findViewById(R.id.buttonRemoveTime);
        timeTextView.setText(getString(R.string.description_time));

        timeTextView.setOnClickListener(v -> showTimePickerDialog(timeTextView));


        buttonAdd.setOnClickListener(v -> addTimeRow(false));

        if (isFirstRow) {
            buttonRemove.setVisibility(View.INVISIBLE);
        } else {
            buttonRemove.setVisibility(View.VISIBLE);
            buttonRemove.setOnClickListener(v -> {
                if (linearLayoutTimes.getChildCount() > 1) {
                    linearLayoutTimes.removeView(timeRow);
                }
            });
        }

        linearLayoutTimes.addView(timeRow);
    }
    /**
     * Shows a time picker dialog and updates the given TextView with the selected time.
     *
     * @param newTimeTextView the TextView to update with the selected time
     */
    private void showTimePickerDialog(TextView newTimeTextView) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateReminderActivity.this,
                (timePicker, hourOfDay, minute1) -> {
                    String formattedTime = String.format("%02d:%02d", hourOfDay, minute1);
                    newTimeTextView.setText(formattedTime);

                },
                hour, minute, true
        );
        timePickerDialog.show();
    }
    /**
     * Validates inputs and creates reminders in the database with selected times.
     * Shows success dialog or error messages accordingly.
     */
    private void createReminder() {

        if (!validateInput()) return;

        Reminder reminder = prepareReminderData();
        String selectedFrequency = spinnerFrequency.getSelectedItem().toString();
        String frequencyCode;

// Convert localized label to fixed code
        if (selectedFrequency.equals(getString(R.string.daily))) {
            frequencyCode = "DAILY";
        } else if (selectedFrequency.equals(getString(R.string.weekly))) {
            frequencyCode = "WEEKLY";
        } else if (selectedFrequency.equals(getString(R.string.monthly))) {
            frequencyCode = "MONTHLY";
        } else if (selectedFrequency.equals(getString(R.string.yearly))) {
            frequencyCode = "YEARLY";
        } else {
            frequencyCode = "DAILY";
        }

        reminder.setFrequency(frequencyCode);



        long result = -1;
        ArrayList<String> validTimes = new ArrayList<>();
        // First validate and collect all times
        for (int i = 0; i < linearLayoutTimes.getChildCount(); i++) {
            View row = linearLayoutTimes.getChildAt(i);
            TextView timeText = row.findViewById(R.id.textViewTime);
            String time = timeText.getText().toString();
            if (!time.equals(getString(R.string.description_time))) {
                validTimes.add(time);
            }
        }

        // Save each time as separate reminder with same groupId
        if (!validTimes.isEmpty()) {
            long groupId = System.currentTimeMillis(); // Generate unique group ID

            for (String time : validTimes) {
                Reminder timeReminder = new Reminder(reminder); // Copy constructor
                timeReminder.setGroupId(groupId);
                timeReminder.setTime(time);
                result = dbHelper.addReminder(timeReminder);

                if (result == -1) {
                    Toast.makeText(this, getString(R.string.fail_save_reminder), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            showSuccessDialog();
            clearFields();
        } else {
            Toast.makeText(this, getString(R.string.select_time), Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Validates that all required fields have been filled and dates and times are selected.
     *
     * @return true if all inputs are valid, false otherwise
     */

    private boolean validateInput() {
        if (editTextName.getText().toString().trim().isEmpty() ||
                editTextDescription.getText().toString().trim().isEmpty() ||
                editTextDosage.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.fill_all_fields));
            return false;
        }

        if (textViewStartDateValue.getText().toString().equals(getString(R.string.description_startdate)) ||
                textViewEndDateValue.getText().toString().equals(getString(R.string.description_enddate))) {
            showToast(getString(R.string.select_dates));
            return false;
        }

        if (!hasValidTimeSelected()) {
            showToast(getString(R.string.select_time));
            return false;
        }

        return true;
    }
    /**
     * Checks if at least one valid time has been selected.
     *
     * @return true if a valid time is selected, false otherwise
     */
    private boolean hasValidTimeSelected() {
        for (int i = 0; i < linearLayoutTimes.getChildCount(); i++) {
            View row = linearLayoutTimes.getChildAt(i);
            TextView timeText = row.findViewById(R.id.textViewTime);
            if (!timeText.getText().toString().equals(getString(R.string.description_time))) {
                return true;
            }
        }
        return false;
    }
    /**
     * Prepares a Reminder object from the current UI input values.
     *
     * @return the prepared Reminder object
     */
    private Reminder prepareReminderData() {
        Reminder reminder = new Reminder();

        reminder.setName(editTextName.getText().toString().trim());
        reminder.setDescription(editTextDescription.getText().toString().trim());
        reminder.setDosage(editTextDosage.getText().toString().trim());
        reminder.setFrequency(spinnerFrequency.getSelectedItem().toString());
        String displayStart = textViewStartDateValue.getText().toString();
        String displayEnd = textViewEndDateValue.getText().toString();
        reminder.setDisplayStartDate(displayStart);
        reminder.setDisplayEndDate(displayEnd);
        reminder.setStartDate(convertToDatabaseDate(displayStart));
        reminder.setEndDate(convertToDatabaseDate(displayEnd));
        reminder.setIsCompleted(false);
        reminder.setIsNotified(false);
        return reminder;
    }
    /**
     * Converts a date string from display format "dd/MM/yyyy" to database format "yyyy-MM-dd".
     *
     * @param displayDate date string in "dd/MM/yyyy" format
     * @return date string in "yyyy-MM-dd" format or original if parsing fails
     */
    private String convertToDatabaseDate(String displayDate) {
        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inFormat.parse(displayDate);
            return outFormat.format(date);
        } catch (Exception e) {
            return displayDate;
        }
    }
    /**
     * Clears all input fields and resets the UI to its initial state.
     */
    private void clearFields() {

        editTextName.setText("");
        editTextDescription.setText("");
        editTextDosage.setText("");
        textViewStartDateValue.setText(getString(R.string.description_startdate));
        textViewEndDateValue.setText(getString(R.string.description_enddate));
        spinnerFrequency.setSelection(0);

        linearLayoutTimes.removeAllViews();
        initTimeRows();
    }
    /**
     * Shows a success dialog after a reminder is successfully saved.
     */

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.success))
                .setMessage(getString(R.string.reminder_success))
                .setPositiveButton(getString(R.string.ok), (dialog, which) -> dialog.dismiss())
                .show();
    }
    /**
     * Shows a short Toast message.
     *
     * @param message the message to display
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    /**
     * Handles the action bar's back button click.
     *
     * @return true to indicate the event was handled
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}