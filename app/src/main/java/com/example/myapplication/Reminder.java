
package com.example.myapplication;

import java.util.ArrayList;

public class Reminder {
    private long id;
    private String name;
    private String description;
    private String dosage;
    private String frequency;
    private String startDate;       // 数据库格式 yyyy-MM-dd
    private String endDate;         // 数据库格式 yyyy-MM-dd
    private String displayStartDate; // 显示格式 dd/MM/yyyy
    private String displayEndDate;   // 显示格式 dd/MM/yyyy
    private ArrayList<String> times; // HH:mm 格式

    // 空构造器（数据库操作需要）
    public Reminder() {}

    // Getter 和 Setter 方法
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }



    public ArrayList<String> getTimes() { return times; }
    public void setTimes(ArrayList<String> times) { this.times = times; }
}