package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
/**
 * {@code ReminderAdapter} is a RecyclerView adapter responsible for displaying a list of reminders
 * in a RecyclerView. It binds reminder data to views defined in {@code reminder_item.xml} layout.
 * <p>
 * It also handles item click events and checkbox state changes using the {@link OnReminderClickListener} interface.
 */
public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    //The generic type specifies that the internally defined ViewHolder type is used to control each row of views.
    private List<Reminder> reminders;
    private final OnReminderClickListener listener;

    /**
     * Interface for handling click events on a reminder item and checkbox changes.
     */
    public interface OnReminderClickListener {
        /**
         * Called when a reminder item is clicked.
         *
         * @param reminder the clicked reminder
         */
        void onReminderClick(Reminder reminder);
        /**
         * Called when the completed checkbox state changes.
         *
         * @param reminder  the reminder whose checkbox changed
         * @param isChecked the new checked state
         */
        void onCheckedChanged(Reminder reminder, boolean isChecked);

    }
    /**
     * Constructs a new ReminderAdapter.
     *
     * @param reminders the initial list of reminders to display
     * @param listener  the listener for item clicks and checkbox state changes
     */
    public ReminderAdapter(List<Reminder> reminders, OnReminderClickListener listener) {
        this.reminders = reminders;
        this.listener = listener;
    }
    /**
     * Updates the list of reminders displayed and notifies the adapter to refresh.
     *
     * @param newReminders the new list of reminders
     */
    public void updateData(List<Reminder> newReminders) {
        this.reminders = newReminders;
        notifyDataSetChanged();
    }
    /**
     * Creates a new ViewHolder for a reminder item.
     *
     * @param parent   the parent ViewGroup
     * @param viewType the view type (not used here)
     * @return a new ViewHolder for the reminder item
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item, parent, false);
        //Convert the reminder_item.xml file into a View object for each item displayed in RecyclerView.
        //LayoutInflater is a tool for converting XML layout files into View objects.
        //false: Do not add it to the parent View immediately (the system decides when to add it)
        return new ViewHolder(view);
    }
    /**
     * Binds reminder data to the given ViewHolder.
     *
     * @param holder   the ViewHolder to bind data to
     * @param position the position of the item in the data list
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);
        holder.tvName.setText(reminder.getName());
        holder.tvTime.setText(reminder.getTime() != null ? reminder.getTime() : "");
        holder.tvDosage.setText(reminder.getDosage());
        boolean isCompleted = reminder.getIsCompleted();
        holder.itemView.setActivated(isCompleted);
        holder.cbCompleted.setChecked(isCompleted);

        if(reminder.getIsCompleted()){
            holder.itemView.setActivated(true);
        }else{
            holder.itemView.setActivated(false);
        }
        holder.cbCompleted.setChecked(reminder.getIsCompleted());//Map the completion state (true/false) of the current reminder to the check state of the CheckBox.
        holder.itemView.setOnClickListener(v -> listener.onReminderClick(reminder));

        holder.cbCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {//Add a listener for cbCompleted (the checkbox in the completed state) to monitor whether it is checked or unchecked.
            listener.onCheckedChanged(reminder, isChecked);
            //Call the passed listener to notify: "this reminder is now checked/cancelled".
            holder.itemView.setActivated(isChecked);
        });
        //When the user checks or unchecks this CheckBox (cbCompleted), an event listener is triggered
        // to perform related operations: notify the listener of the state change and highlight the current item view .
    }
    /**
     * Returns the total number of reminder items.
     *
     * @return the item count
     */
    @Override
    public int getItemCount() {
        return reminders.size();
    }
    /**
     * ViewHolder class that holds views for a single reminder item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTime, tvDosage;
        CheckBox cbCompleted;
        /**
         * Constructs a new ViewHolder and initializes UI components.
         *
         * @param itemView the root view of the item layout
         */
        public ViewHolder(View itemView) {
            super(itemView);
            tvName =itemView.findViewById(R.id.tvReminderName);
            tvTime =itemView.findViewById(R.id.tvTime);
            tvDosage =itemView.findViewById(R.id.tvDosage);
            cbCompleted =itemView.findViewById(R.id.cbCompleted);
        }
    }
}