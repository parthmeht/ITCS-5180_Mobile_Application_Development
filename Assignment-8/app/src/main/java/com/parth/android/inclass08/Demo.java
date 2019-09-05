/*
package com.mad.demo.firebasedemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TaskAdapter extends ArrayAdapter<Task> {
    ModifyTask modifyTask;
    ArrayList<Task> tasks = new ArrayList<>();
    Context context;
    public TaskAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Task> objects, ModifyTask m) {
        super(context, resource, objects);
        this.context = context;
        this.tasks = objects;
        this.modifyTask = m;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        final Task t = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todolist_structure, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.taskName = convertView.findViewById(R.id.taskName);
            viewHolder.priority = convertView.findViewById(R.id.priority);
            viewHolder.time = convertView.findViewById(R.id.time);
            viewHolder.checkBox = convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.taskName.setText(t.taskName);
        viewHolder.priority.setText(t.priority);
        PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        originalFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();
        try {
            date = originalFormat.parse(t.time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.time.setText(prettyTime.format(date));
        viewHolder.checkBox.setChecked(t.checked);

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    t.checked = true;
                    t.status = "Completed";
                }else{
                    t.checked = false;
                    t.status = "Pending";
                }
                modifyTask.modifyTaskOnCheckBox(t);
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                modifyTask.deleteTask(t);
                return false;
            }
        });
        return  convertView;
    }

    class ViewHolder {
        TextView taskName;
        TextView priority;
        TextView time;
        CheckBox checkBox;
    }

    interface ModifyTask{
     void modifyTaskOnCheckBox(Task task);
     void deleteTask(Task task);
    }
}
*/
