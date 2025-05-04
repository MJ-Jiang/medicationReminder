package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ChartSettingsActivity extends AppCompatActivity {
    private TextView textViewSelectedStartDate;
    private TextView textViewSelectedEndDate;
    private BarChart barChart;
    private ReminderDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        if (getSupportActionBar() != null) {//先检查一下，当前界面有没有 ActionBar（标题栏）
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//在标题栏上，显示一个返回按钮（一般是左上角的 ← 小箭头）
            getSupportActionBar().setTitle(R.string.setting_chart);  // 设置标题
        }
        // Initialize views
        textViewSelectedStartDate = findViewById(R.id.textViewSelectedStartDate);
        textViewSelectedEndDate = findViewById(R.id.textViewSelectedEndDate);
        barChart = findViewById(R.id.barChart);
        dbHelper = new ReminderDatabaseHelper(this);
        setDefaultDateRange();
        setupChart();
        loadChartData();
    }
    private void setDefaultDateRange(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        // 结束日期设为今天
        String endDate = sdf.format(calendar.getTime());
        textViewSelectedEndDate.setText(endDate);

        // 开始日期设为7天前
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        String startDate = sdf.format(calendar.getTime());
        textViewSelectedStartDate.setText(startDate);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void setupChart() {
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularity(1f);

        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(true);
    }

    private void loadChartData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        // 获取结束日期（今天）
        String endDateStr = textViewSelectedEndDate.getText().toString();
        Date endDate;
        try {
            endDate = sdf.parse(endDateStr);
            calendar.setTime(endDate);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // 获取开始日期（7天前）
        String startDateStr = textViewSelectedStartDate.getText().toString();
        Date startDate;
        try {
            startDate = sdf.parse(startDateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // 准备X轴标签（日期）
        List<String> dates = new ArrayList<>();
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(startDate);

        while (!tempCal.getTime().after(endDate)) {
            dates.add(sdf.format(tempCal.getTime()));
            tempCal.add(Calendar.DAY_OF_YEAR, 1);
        }

        // 查询数据
        List<BarEntry> allReminderEntries = new ArrayList<>();
        List<BarEntry> completedEntries = new ArrayList<>();

        for (int i = 0; i < dates.size(); i++) {
            String date = dates.get(i);
            int allCount = dbHelper.getAllRemindersCountForDate(date);
            int completedCount = dbHelper.getCompletedRemindersCountForDate(date);

            allReminderEntries.add(new BarEntry(i, allCount));
            completedEntries.add(new BarEntry(i, completedCount));
        }

        // 创建数据集
        BarDataSet allDataSet = new BarDataSet(allReminderEntries,getString(R.string.chart_all));
        allDataSet.setColor(Color.BLUE);

        BarDataSet completedDataSet = new BarDataSet(completedEntries, getString(R.string.chart_completed));
        completedDataSet.setColor(Color.GREEN);

        // 组合数据
        BarData barData = new BarData(allDataSet, completedDataSet);
        barData.setBarWidth(0.3f); // 设置柱状图宽度

        // 设置X轴标签
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));

        // 应用数据
        barChart.setData(barData);
        barChart.groupBars(0, 0.1f, 0.05f); // 调整柱状图分组
        barChart.invalidate(); // 刷新图表
    }
}
