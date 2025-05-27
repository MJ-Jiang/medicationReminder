# Medicine Reminder App - Component Overview

1. **Reminder**  
   Represents a medication reminder item, encapsulating all relevant data for a single reminder. Implements Android’s `Parcelable` interface for easy passing between components like Activities or Services. Contains fields such as ID, group ID, time, name, dosage, frequency, start/end dates, display start/end dates, completion status (`isCompleted`), and notification status (`isNotified`). Provides getter and setter methods for every field.

2. **ReminderQueryHelper**  
   A database utility class that simplifies querying reminders stored in an SQLite database for the app. Key responsibilities include:
    - Retrieving reminders filtered by date, name, or other criteria.
    - Counting reminders overall or by completion status with various filters.
    - Fetching unique reminder names.
    - Updating notification status to mark reminders as notified or not.

3. **ReminderDatabaseHelper**  
   A subclass of `SQLiteOpenHelper` that manages the creation, upgrading, insertion, and querying of the local SQLite database for reminders and reminder groups. Key responsibilities include:
    - Setting up two tables: `reminders` (individual entries) and `reminder_groups` (groups of reminders with times stored as JSON arrays).
    - Inserting new reminders and updating corresponding groups while handling repeated reminders based on frequency (DAILY, WEEKLY, etc.).
    - Updating reminders’ completion and notification statuses.
    - Collaborating with `ReminderQueryHelper` to handle query logic while managing database access.

4. **ReminderLoader**  
   Provides utility methods to load reminders for a specific date via `ReminderDatabaseHelper`, returning reminders sorted by time in chronological order.

5. **ReminderAdapter**  
   Connects a list of `Reminder` objects to a `RecyclerView`, efficiently displaying each reminder’s name, time, dosage, and completion status. It handles user interactions via a listener interface that dynamically updates the UI and communicates changes externally. The adapter supports dynamic updates through an `updateData()` method, which replaces the current list and refreshes the display. It also visually indicates a reminder’s completion by toggling the item’s activated state, providing clear feedback to users.

6. **ReminderDetailDialog**  
   A helper class designed to display detailed information about a specific `Reminder` in a dialog box. The details include name, description, dosage, frequency, active date range, and scheduled times, providing users with an informative overview.

7. **DatePickerHelper**  
   Offers utility methods to display a date picker dialog and manage user date selection across various activities. It updates a target `TextView` with the chosen date and optionally refreshes related data, such as reminder lists or chart visualizations. The class provides overloaded `show()` methods tailored for use in `MainActivity`, `CreateReminderActivity`, and `ChartSettingsActivity`, ensuring reminders or chart data are reloaded as needed. Additionally, it includes a simplified method for displaying just the date picker dialog when only UI date display is required.

8. **CreateReminderActivity**  
   Allows users to create new medication reminders through a structured form interface. Key responsibilities include:
    - Initializing UI components including input fields, date pickers, frequency spinner, and multiple time picker rows with add/remove controls.
    - Validating user input and formatting dates for database storage.
    - Inserting one reminder per selected time with a shared group ID into the database.
    - Providing user feedback via dialogs and toasts.

9. **SettingsActivity**  
   Manages the app’s settings screen by initializing the UI and configuring the action bar with a back button and a title. It attaches click listeners to key settings options—such as language settings and chart settings—allowing users to navigate easily to `LanguageSettingsActivity` or `ChartSettingsActivity`. The activity also handles the action bar’s “up” button to support smooth back navigation to the previous screen.

10. **LanguageSettingsActivity**  
    Lets users choose the app’s language through simple UI buttons (like English and Finnish). It updates the app’s locale based on the selected language code, saves this preference using `SharedPreferences`, and then restarts `MainActivity` to immediately apply the language change across the app. The activity also sets up the action bar with a back button and a title and handles the navigation when the user presses the “up” button to return to the previous screen.

11. **ChartsSettingsActivity**  
    This activity displays medication reminder statistics using a bar chart, letting users explore their data easily by selecting custom date ranges and filtering by reminder names. By default, it shows data for all reminders from the past seven days ending today, including both overall counts and completed counts. Users can also specify a date range and reminder name to focus on specific data that matters most to them.

12. **ReminderNotifier**  
    Responsible for checking today’s medication reminders and showing timely notification dialogs for those not yet completed or notified, matching the current time. It uses the app context and database helper to query reminders and update their notification status. The main function, `checkAndNotifyReminders()`, fetches today’s reminders, filters out completed ones, and displays alerts on the main thread when it’s time to take pills. It also marks reminders as notified to avoid repeated alerts. The private helper method `isTimeToNotify()` compares the current time (in Helsinki timezone) with each reminder’s scheduled time to decide when to notify.

13. **MainActivity**  
    The central activity that displays the user’s medication reminders for a selected date in a `RecyclerView`. Key responsibilities include:
    - Initializing the UI components, database helper, and adapter to display reminders.
    - Allowing date selection via a date picker and updating the displayed reminder list accordingly.
    - Handling user interactions such as creating new reminders, opening settings, and clicking on reminder items to show detailed dialogs.
    - Managing reminder completion status changes and updating the database.
    - Periodically checking for due reminders and showing notification dialogs using `ReminderNotifier` on the main thread every minute.
    - Applying the user’s language preference by overriding `attachBaseContext` to update the app locale dynamically.
    - Refreshing the reminder list when the activity resumes to keep data up to date.
