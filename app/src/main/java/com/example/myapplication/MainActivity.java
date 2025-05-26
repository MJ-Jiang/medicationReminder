package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * {@code MainActivity} is the main screen of the reminder app.
 * It displays a list of reminders for the selected date, allows the user to
 * select dates, create new reminders, and access settings.
 *
 * <p>This activity uses a {@link RecyclerView} with a {@link ReminderAdapter}
 * to show reminders and listens for reminder item clicks and completion changes.</p>
 *
 * <p>It also periodically checks for reminders that need notification using
 * {@link ReminderNotifier}.</p>
 *
 * <p>Language settings are applied dynamically when attaching the base context.</p>
 */
public class MainActivity extends AppCompatActivity implements ReminderAdapter.OnReminderClickListener {
    private RecyclerView recyclerView;//Efficiently display large numbers of data items in scrolling lists or grids.
    private ReminderAdapter adapter;
    private ReminderDatabaseHelper dbHelper;
    private TextView textViewSelectedDate;
    private ReminderNotifier notifier;
    private Handler handler =new Handler(Looper.getMainLooper());
    /**
     * Initializes the activity, including the UI, database, adapter, date picker,
     * notification checks, and event listeners.
     *
     * @param savedInstanceState saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new ReminderDatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerViewReminders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReminderAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        textViewSelectedDate = findViewById(R.id.textViewSelectedDate);
        Button buttonCreateReminder = findViewById(R.id.buttonCreateReminder);
        ImageView buttonSelectDate = findViewById(R.id.buttonSelectDate);
        FloatingActionButton fabSettings = findViewById(R.id.fabSettings);


        textViewSelectedDate.setText(Utils.getTodayDate());
        updateReminderList(Utils.getTodayDate());

        buttonCreateReminder.setOnClickListener(v -> startActivity(new Intent(this, CreateReminderActivity.class)));

        buttonSelectDate.setOnClickListener(v ->
                DatePickerHelper.show(this, textViewSelectedDate.getText().toString(), textViewSelectedDate, adapter, dbHelper));
        fabSettings.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SettingsActivity.class)));

        dbHelper = new ReminderDatabaseHelper(this);
        notifier = new ReminderNotifier(this, dbHelper);
        Runnable reminderCheckRunnable = new Runnable() {
            @Override
            public void run() {
                notifier.checkAndNotifyReminders();
                handler.postDelayed(this, 60000);//Execute itself again after 60 seconds
            }
        };
        handler.post(reminderCheckRunnable);//Give the task just defined to the handler to execute immediately

    }
    /**
     * Updates the displayed reminder list for the given date.
     *
     * @param date the date string in format yyyy-MM-dd
     */
    private void updateReminderList(String date){
        List<Reminder> reminders=ReminderLoader.loadRemindersForDate(dbHelper,date);
        adapter.updateData(reminders);

    }

    /**
     * Handles clicks on a reminder item, showing its detail dialog.
     *
     * @param reminder the clicked reminder
     */
    @Override
    public void onReminderClick(Reminder reminder) {

        List<String> times = dbHelper.getGroupTimes(reminder.getGroupId());
        ReminderDetailDialog.show(this, reminder, times);
    }
    /**
     * Handles changes to a reminder's completion status and updates the database.
     *
     * @param reminder the reminder whose status changed
     * @param isChecked true if completed, false otherwise
     */
    @Override
    public void onCheckedChanged(Reminder reminder, boolean isChecked) {

        dbHelper.updateReminderCompletion(reminder.getId(), isChecked);
    }
    /**
     * Refreshes the reminder list when the activity resumes.
     */

    @Override
    protected void onResume() {
        super.onResume();
        updateReminderList(textViewSelectedDate.getText().toString());
    }
    /**
     * Applies user-selected language preference when attaching the base context.
     *
     * @param base the original base context
     */
    @Override
    protected void attachBaseContext(Context base) {
        SharedPreferences prefs = base.getSharedPreferences("settings", MODE_PRIVATE);
        //Open a SharedPreferences file named "settings" from the passed context base, in private mode
        String lang = prefs.getString("language", "en");
        Locale newLocale = new Locale(lang);
        Locale.setDefault(newLocale);

        Configuration config = base.getResources().getConfiguration();
        //Get the current Configuration through the base resource manager, which contains information such as the device's language, screen size, orientation, etc.
        config.setLocale(newLocale);

        super.attachBaseContext(base.createConfigurationContext(config));
    }
    //attachBaseContext is a lifecycle method in ContextWrapper and Activity that allows to wrap the original Context with a custom configuration

}
