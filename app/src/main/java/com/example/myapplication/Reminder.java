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
    private boolean isCompleted;
    // 空构造器（数据库必需）
    public Reminder() {}
    public Reminder(Reminder other){
        this.id=other.id;
        this.name=other.name;
        this.description=other.description;
        this.dosage=other.dosage;
        this.frequency=other.frequency;
        this.startDate=other.startDate;
        this.endDate=other.endDate;
        this.displayStartDate= other.displayStartDate;
        this.displayEndDate= other.displayEndDate;
        this.times = new ArrayList<>(other.times);
        this.isCompleted=other.isCompleted;

    }

    // Parcelable实现（用于Activity间传递）
    protected Reminder(Parcel in) {
        //一个超高效的存储箱，可以把各种数据（数字、字符串、对象等）打包起来，然后传到别的地方。通过Intent、Bundle传递。
        //Parcel in 里已经打包好了一份 Reminder 对象的数据。接下来是打开这个盒子（in），把里面的数据一个一个拿出来，恢复成一个新的 Reminder 对象。
        id = in.readLong();//从 Parcel 中读取一个 长整型（long） 值，赋给 id 这个字段
        name = in.readString();
        description = in.readString();
        dosage = in.readString();
        frequency = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        displayStartDate = in.readString();
        displayEndDate = in.readString();
        times = in.createStringArrayList();
        isCompleted = in.readByte() != 0;
    }

    public static final Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel in) {
            return new Reminder(in);
        }//Creator 是 Android 官方定义的一个接口类，在 android.os.Parcelable 里面。告诉 Android，怎么根据一个 Parcel 快递盒子，创建出一个新的 Reminder

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }//创建一个 Reminder 数组，长度是 size。
    };

    @Override
    public int describeContents() {
        return 0;
    }//是 Parcelable 必须实现的方法之一。通常返回 0，告诉系统没特殊情况

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //把 Reminder 里面的每一份数据，一份一份放进 dest 这个快递盒子里（Parcel）
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(dosage);
        dest.writeString(frequency);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(displayStartDate);
        dest.writeString(displayEndDate);
        dest.writeByte((byte) (isCompleted ? 1 : 0));
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
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public String getDisplayStartDate() { return displayStartDate; }
    public void setDisplayStartDate(String displayStartDate) { this.displayStartDate = displayStartDate; }

    public String getDisplayEndDate() { return displayEndDate; }
    public void setDisplayEndDate(String displayEndDate) { this.displayEndDate = displayEndDate; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }



    public ArrayList<String> getTimes() { return times; }
    public void setTimes(ArrayList<String> times) { this.times = times; }
    public boolean getIsCompleted() { return isCompleted; }
    public void setIsCompleted(boolean isCompleted) { this.isCompleted = isCompleted; }
}