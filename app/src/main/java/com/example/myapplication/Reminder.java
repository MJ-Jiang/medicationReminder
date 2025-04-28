package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Reminder implements Parcelable {
    private long id;
    private String name;
    private String description;
    private String dosage;
    private String frequency;
    private String startDate;       // 存储格式：yyyy-MM-dd
    private String endDate;         // 存储格式：yyyy-MM-dd
    private String displayStartDate; // 显示格式：dd/MM/yyyy
    private String displayEndDate;   // 显示格式：dd/MM/yyyy
    private ArrayList<String> times; // 格式：HH:mm

    // 空构造器（数据库必需）
    public Reminder() {}

    // Parcelable实现（用于Activity间传递）
    protected Reminder(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        dosage = in.readString();
        frequency = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        displayStartDate = in.readString();
        displayEndDate = in.readString();
        times = in.createStringArrayList();
    }

    public static final Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel in) {
            return new Reminder(in);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(dosage);
        dest.writeString(frequency);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(displayStartDate);
        dest.writeString(displayEndDate);
        dest.writeStringList(times);
    }

    // Getter & Setter
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String dosage) { this.frequency = frequency; }

    public String getDisplayStartDate() { return displayStartDate; }
    public void setDisplayStartDate(String dosage) { this.displayStartDate = displayStartDate; }

    public String getDisplayEndDate() { return displayEndDate; }
    public void setDisplayEndDate(String dosage) { this.displayEndDate = displayEndDate; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String dosage) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String dosage) { this.endDate = endDate; }



    public ArrayList<String> getTimes() { return times; }
    public void setTimes(ArrayList<String> times) { this.times = times; }
}