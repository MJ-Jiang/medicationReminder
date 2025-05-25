package com.example.myapplication;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a reminder item for tasks such as medication schedules.
 * Implements {@link Parcelable} to allow easy passing between Android components.
 */

public class Reminder implements Parcelable {
    private long id;
    private long groupId;
    private String time;
    private String name;
    private String description;
    private String dosage;
    private String frequency;
    private String startDate;
    private String endDate;
    private String date;
    private String displayStartDate;
    private String displayEndDate;
    private boolean isCompleted;
    private boolean isNotified;

    /**
     * Default constructor. Initializes the groupId using the current system time.
     */
    public Reminder() {

        groupId=System.currentTimeMillis();
    }

    /**
     * Copy constructor.
     *
     * @param other The Reminder object to copy.
     */
    public Reminder(Reminder other){
        this.id=other.id;
        this.groupId=other.groupId;
        this.time=other.time;
        this.name=other.name;
        this.description=other.description;
        this.dosage=other.dosage;
        this.frequency=other.frequency;
        this.startDate=other.startDate;
        this.endDate=other.endDate;
        this.displayStartDate= other.displayStartDate;
        this.displayEndDate= other.displayEndDate;
        this.date=other.date;
        this.isCompleted=other.isCompleted;
        this.isNotified = other.isNotified;
    }

    /**
     * Constructor to recreate a Reminder from a {@link Parcel}.
     *
     * @param in Parcel containing the Reminder data.
     */
    protected Reminder(Parcel in) {
        //A super efficient storage box that can package various data (numbers, strings, objects, etc.) and then pass them to other places. Pass them through Intent and Bundle.
        //Parcel in has packed a Reminder object's data. Next, open the box (in), take out the data one by one, and restore it into a new Reminder object.
        //This constructor reads back the data from the Parcel:
        id = in.readLong();//Read a long value from Parcel and assign it to the id field
        groupId=in.readLong();
        time = in.readString();
        name =in.readString();
        description = in.readString();
        dosage = in.readString();
        frequency =in.readString();
        startDate = in.readString();
        endDate =in.readString();
        date=in.readString();
        displayStartDate = in.readString();
        displayEndDate = in.readString();
        isCompleted =in.readByte() !=0;
        isNotified =in.readByte() !=0;

    }

    /**
     * Parcelable creator to generate instances of Reminder from a Parcel.
     */
    public static final Creator<Reminder> CREATOR =new Creator<Reminder>(){
        @Override
        public Reminder createFromParcel(Parcel in){
            return new Reminder(in);
        }//Creator is an interface class officially defined by Android, in android.os.Parcelable. It tells Android how to create a new Reminder based on a Parcel box.

        @Override
        public Reminder[] newArray(int size){
            return new Reminder[size];
        }
    };

    /**
     * Describes the kinds of special objects contained in this Parcelable instance.
     *
     * @return an integer bitmask indicating special object types. Returns 0 if none.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the Reminder object's data to the given Parcel.
     *
     * @param dest  The Parcel in which the data should be written.
     * @param flags Additional flags about how the object should be written.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //Put each piece of data in Reminder into the courier box dest (Parcel) one by one
        //To put Reminder into the Intent, Android serializes it into a Parcel
        dest.writeLong(id);
        dest.writeLong(groupId);
        dest.writeString(time);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(dosage);
        dest.writeString(frequency);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(date);
        dest.writeString(displayStartDate);
        dest.writeString(displayEndDate);
        dest.writeByte((byte) (isCompleted ? 1 : 0));
        dest.writeByte((byte) (isNotified ? 1 : 0));
    }

    // Getter & Setter
    /** @return the unique ID of the reminder */
    public long getId() { return id; }

    /** @return the unique ID of the reminder */
    public void setId(long id) { this.id = id; }

    /** @return the group ID used to group related reminders */
    public long getGroupId() { return groupId; }

    /** @param groupId the group ID to set */
    public void setGroupId(long groupId) { this.groupId = groupId; }

    /** @return the time of the reminder */
    public String getTime() { return time; }

    /** @param time the time to set */
    public void setTime(String time) { this.time = time; }

    /** @return the name/title of the reminder */
    public String getName() { return name; }

    /** @param name the name/title to set */
    public void setName(String name) { this.name = name; }

    /** @return the description of the reminder */
    public String getDescription() { return description; }

    /** @param description the description to set */
    public void setDescription(String description) { this.description = description; }

    /** @return the dosage information */
    public String getDosage() { return dosage; }

    /** @param dosage the dosage to set */
    public void setDosage(String dosage) { this.dosage = dosage; }

    /** @return the frequency of the reminder */
    public String getFrequency() { return frequency; }

    /** @param frequency the frequency to set */
    public void setFrequency(String frequency) { this.frequency = frequency; }

    /** @return the date of the reminder */
    public String getDate() {return date;}
    /** @param date the frequency to set */
    public void setDate(String date) {this.date = date;}
    /** @return the formatted display start date */
    public String getDisplayStartDate() { return displayStartDate; }

    /** @param displayStartDate the display start date to set */
    public void setDisplayStartDate(String displayStartDate) { this.displayStartDate = displayStartDate; }

    /** @return the formatted display end date */
    public String getDisplayEndDate() { return displayEndDate; }

    /** @param displayEndDate the display end date to set */
    public void setDisplayEndDate(String displayEndDate) { this.displayEndDate = displayEndDate; }

    /** @return the internal start date */
    public String getStartDate() { return startDate; }

    /** @param startDate the internal start date to set */
    public void setStartDate(String startDate) { this.startDate = startDate; }

    /** @return the internal end date */
    public String getEndDate() { return endDate; }

    /** @param endDate the internal end date to set */
    public void setEndDate(String endDate) { this.endDate = endDate; }

    /** @return true if the reminder is marked as completed */
    public boolean getIsCompleted() { return isCompleted; }

    /** @param isCompleted sets whether the reminder is completed */
    public void setIsCompleted(boolean isCompleted) { this.isCompleted = isCompleted; }

    /** @return true if the reminder has been notified */
    public boolean getIsNotified() { return isNotified; }

    /** @param isNotified sets whether the reminder has been notified */
    public void setIsNotified(boolean isNotified) { this.isNotified = isNotified; }

}