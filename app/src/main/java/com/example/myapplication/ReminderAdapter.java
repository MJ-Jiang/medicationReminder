package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    //泛型指定使用的是内部定义的 ViewHolder 类型，负责控制每一行视图
    private List<Reminder> reminders;
    private final OnReminderClickListener listener;//接口对象，用于监听每条提醒的点击和 checkbox 变化事件。
//定义一个接口，用于监听提醒项的点击行为和勾选框变化。
    public interface OnReminderClickListener {
        void onReminderClick(Reminder reminder);
        void onCheckedChanged(Reminder reminder, boolean isChecked);
    }

    public ReminderAdapter(List<Reminder> reminders, OnReminderClickListener listener) {
        this.reminders = reminders;
        this.listener = listener;
    }

    public void updateData(List<Reminder> newReminders) {
        this.reminders = newReminders;
        notifyDataSetChanged();//通知 RecyclerView 重新刷新整个列表。
    }
//用于创建每个 item 的布局视图。
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_item, parent, false);//LayoutInflater 加载 reminder_item.xml
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);//获取对应位置的数据
        holder.tvName.setText(reminder.getName());//提醒名到 TextView

        // 显示第一个时间（后续会展开多个）
        if (!reminder.getTimes().isEmpty()) {
            holder.tvTime.setText(reminder.getTimes().get(0));
        }
        holder.tvDosage.setText(reminder.getDosage());

        holder.itemView.setOnClickListener(v -> {
            listener.onReminderClick(reminder);
        });

        holder.cbCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            listener.onCheckedChanged(reminder, isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTime, tvDosage;
        CheckBox cbCompleted;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvReminderName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDosage = itemView.findViewById(R.id.tvDosage);
            cbCompleted = itemView.findViewById(R.id.cbCompleted);
        }
    }
}