package com.parth.android.inclass08;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class TaskAdapter extends ArrayAdapter<Task> {

    private ArrayList<Task> tasks;
    private Context context;
    private TaskOperations taskOperations;

    public TaskAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Task> objects, TaskOperations taskOperations) {
        super(context, resource, objects);
        this.tasks = objects;
        this.context = context;
        this.taskOperations = taskOperations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Task task = (Task) getItem(position);
        if (task != null) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.add_task_layout, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.Note = (TextView) convertView.findViewById(R.id.textViewNote);
                viewHolder.Priority = (TextView) convertView.findViewById(R.id.textViewPriority);
                viewHolder.Time = (TextView) convertView.findViewById(R.id.textViewTime);
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            viewHolder.Note.setText(task.getNote());
            viewHolder.Priority.setText(task.getPriority());
            viewHolder.checkBox.setChecked(task.isCheck());
            String date = messageTime(task);
            if (!(date.equals("") || date.equals(null))) {
                viewHolder.Time.setText(date);
            }

            viewHolder.checkBox.setTag(task.getId());

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkBox = (CheckBox) v;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                    Date convertedDate = new Date();
                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    task.setTime(dateFormat.format(convertedDate));
                    if (checkBox.isChecked()){
                        task.setCheck(true);
                        task.setStatus("Completed");
                    }else {
                        task.setCheck(false);
                        task.setStatus("Pending");
                    }
                    taskOperations.onCheckBoxClick(task);
                }
            });

            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    taskOperations.deleteTask(task);
                    return false;
                }
            });
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView Note;
        TextView Priority;
        TextView Time;
        CheckBox checkBox;

    }

    public String messageTime(Task task) {
        PrettyTime prettyTime = new PrettyTime();

        String dateString = task.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        Date convertedDate = new Date();

        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return prettyTime.format(convertedDate);

    }

    interface TaskOperations {
        void onCheckBoxClick(Task param);
        void deleteTask(Task task);
    }
}
