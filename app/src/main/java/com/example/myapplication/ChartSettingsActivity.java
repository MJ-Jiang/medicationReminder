package com.example.myapplication;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * {@code ChartSettingsActivity} displays a bar chart to visualize reminder statistics
 * such as total and completed reminders within a selected date range. It allows users to
 * select a date range, search for specific reminders by name, and view grouped bar chart data.
 *
 * <p>This activity uses MPAndroidChart for visualization and integrates with a local
 * database via {@link ReminderDatabaseHelper} to fetch reminder data.</p>
 *
 * <p>Users can:
 * <ul>
 *     <li>Select start and end dates for the chart range</li>
 *     <li>Search by reminder name with partial match</li>
 *     <li>View grouped bar chart showing all vs completed reminders</li>
 * </ul>
 */
public class ChartSettingsActivity extends AppCompatActivity {
    private TextView textViewSelectedStartDate;
    private TextView textViewSelectedEndDate;
    private BarChart barChart;
    private ReminderDatabaseHelper dbHelper;
    private TextInputEditText searchBar;
    private String selectedReminderName = null;

    /**
     * Initializes the activity, views, database helper, chart configuration,
     * and sets up event listeners for date selection and search.
     *
     * @param savedInstanceState previously saved state, if any
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.setting_chart);
        }
        // Initialize views
        textViewSelectedStartDate = findViewById(R.id.textViewSelectedStartDate);
        textViewSelectedEndDate = findViewById(R.id.textViewSelectedEndDate);
        barChart = findViewById(R.id.barChart);
        dbHelper = new ReminderDatabaseHelper(this);
        setDefaultDateRange();
        setupChart();
        loadChartData();
        ImageView buttonSelectStartDate = findViewById(R.id.buttonSelectStartDate);
        ImageView buttonSelectEndDate = findViewById(R.id.buttonSelectEndDate);


        buttonSelectStartDate.setOnClickListener(v -> DatePickerHelper.show(this,
                textViewSelectedStartDate.getText().toString(), textViewSelectedStartDate, dbHelper)
        );

        buttonSelectEndDate.setOnClickListener(v -> DatePickerHelper.show(this,
                textViewSelectedEndDate.getText().toString(), textViewSelectedEndDate, dbHelper)
        );

        searchBar = findViewById(R.id.searchBar);
        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

    }
    /**
     * Sets the default date range for the chart to the past 7 days.
     */
    private void setDefaultDateRange(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        String endDate = sdf.format(calendar.getTime());
        textViewSelectedEndDate.setText(endDate);

        calendar.add(Calendar.DAY_OF_YEAR, -7);
        String startDate = sdf.format(calendar.getTime());
        textViewSelectedStartDate.setText(startDate);

    }

    /**
     * Configures the appearance and behavior of the bar chart.
     */
    private void setupChart() {
        barChart.getDescription().setEnabled(false);//Get the chart's description (usually small text in the lower right corner of the chart) and disable it
        barChart.setPinchZoom(false);//Disables the Pinch Zoom feature.
        barChart.setDrawGridBackground(false);//Do not draw the background grid of the chart area (gray lines on a white background)

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//Set the X-axis position to bottom (below the chart)
        xAxis.setGranularity(1f);//Set the minimum spacing between x-axis labels to 1 unit.
        xAxis.setDrawGridLines(false);//Do not draw grid lines along the X axis on the chart

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);//The chart will not display negative areas (bars start at 0
        leftAxis.setGranularity(1f);

        barChart.getAxisRight().setEnabled(false);//Show only the left Y axis
        barChart.getLegend().setEnabled(true);//Enable the legend of the chart
    }
    /**
     * Loads chart data from the database for the selected date range
     * and updates the chart view with total and completed reminders per day.
     */
    public void loadChartData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();//Get a Calendar instance for the current time

        String endDateStr = textViewSelectedEndDate.getText().toString();
        Date endDate;
        try {
            endDate = sdf.parse(endDateStr);
            calendar.setTime(endDate);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String startDateStr = textViewSelectedStartDate.getText().toString();
        Date startDate;
        try {
            startDate = sdf.parse(startDateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        List<String> dates = new ArrayList<>();
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(startDate);
//Create a list to store the date string of each day from the beginning to the end, and use tempCal to iterate day by day starting from startDate
        while (!tempCal.getTime().after(endDate)) {
            dates.add(sdf.format(tempCal.getTime()));
            //If the current date is less than endDate, format the day and add it to the dates list.
            tempCal.add(Calendar.DAY_OF_YEAR, 1);
        }

        List<BarEntry> allReminderEntries = new ArrayList<>();
        List<BarEntry> completedEntries = new ArrayList<>();

        for (int i = 0; i < dates.size(); i++) {
            String date = dates.get(i);
            //If no specific reminder name is selected (null), query the number of all reminders for the day;
            int allCount = selectedReminderName == null ?
                    dbHelper.getAllRemindersCountForDate(date) :
                    dbHelper.getRemindersCountForDateAndName(this,date, selectedReminderName);

            int completedCount = selectedReminderName == null ?
                    dbHelper.getCompletedRemindersCountForDate(date) :
                    dbHelper.getCompletedRemindersCountForDateAndName(this,date, selectedReminderName);

            allReminderEntries.add(new BarEntry(i, allCount));//Create a bar entry (BarEntry):i is the X-axis index;allCount/completedCount are the Y values.
            completedEntries.add(new BarEntry(i, completedCount));
        }

//Create a dataset with the data of "All Alerts" and set the color to blue.
        BarDataSet allDataSet = new BarDataSet(allReminderEntries,getString(R.string.chart_all));
        allDataSet.setColor(Color.BLUE);

        BarDataSet completedDataSet = new BarDataSet(completedEntries, getString(R.string.chart_completed));
        completedDataSet.setColor(Color.GREEN);

//Merge two datasets into one BarData for populating the chart.
        BarData barData = new BarData(allDataSet, completedDataSet);
        barData.setBarWidth(0.3f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));


        barChart.setData(barData);
        barChart.groupBars(0, 0.1f, 0.05f);
        barChart.invalidate();//Refresh the chart, triggering a redraw
    }
    /**
     * Called when the search button is clicked.
     *
     * @param view the view that triggered the click
     */
    public void onSearchClicked(View view) {
        performSearch();
    }
    /**
     * Performs a search in the database for reminders matching the entered name.
     * If matches are found, updates the chart to only show that reminder’s data.
     * If no match is found, shows an alert dialog.
     */
    private void performSearch() {
        String searchText = searchBar.getText().toString().trim();
        if (searchText.isEmpty()) {
            selectedReminderName = null;
            loadChartData();
            return;
        }
        List<String> reminderNames = dbHelper.getAllReminderNames();
        List<String> matchedNames = new ArrayList<>();

        for (String name : reminderNames) {
            if (name.toLowerCase().contains(searchText.toLowerCase())) {
                matchedNames.add(name);
            }
        }
        if (matchedNames.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.chart_cantfind)
                    .setMessage(R.string.chart_cantfinddes)
                    .setPositiveButton(R.string.yes, null)
                    .show();
            return;
        }

        if (matchedNames.size() == 1) {

            selectedReminderName = matchedNames.get(0);
            loadChartData();
        } else {
            showReminderSelectionDialog(matchedNames);
        }
    }
    /**
     * Displays a dialog allowing the user to choose one reminder from multiple matches.
     *
     * @param names the list of matching reminder names
     */
    private void showReminderSelectionDialog(List<String> names) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.chart_search_choose)
                .setItems(names.toArray(new String[0]), (dialog, which) -> {

                    selectedReminderName = names.get(which);

                    searchBar.setText(selectedReminderName);

                    loadChartData();
                })
                .setNegativeButton(R.string.chart_cancel, null)
                .show();
    }
    /**
     * Handles navigation "Up" actions from the toolbar.
     *
     * @return true to indicate navigation was handled
     */

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
