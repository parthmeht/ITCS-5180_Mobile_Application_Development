package com.parth.android.inclassassignment11;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Task extends RealmObject {
    @PrimaryKey
    private int id;
    private String note, priority,time;
    private String status;
    private int priorityId;

    public Task(int id, String note, String priority, String time, String status) {
        this.id = id;
        this.note = note;
        this.priority = priority;
        this.time = time;
        this.status = status;
    }

    public Task(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
        if (priority.equalsIgnoreCase("High"))
            setPriorityId(1);
        else if(priority.equalsIgnoreCase("Medium"))
            setPriorityId(2);
        else if(priority.equalsIgnoreCase("Low"))
            setPriorityId(3);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(int priorityId) {
        this.priorityId = priorityId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", note='" + note + '\'' +
                ", priority='" + priority + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                '}';
    }


}
