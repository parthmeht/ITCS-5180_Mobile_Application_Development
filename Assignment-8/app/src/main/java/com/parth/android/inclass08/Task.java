package com.parth.android.inclass08;

public class Task {
    private String id;
    private String note,priority,time;
    private boolean check;
    private String status;

    public Task(String id, String note, String priority, String time, boolean check, String status) {
        this.id = id;
        this.note = note;
        this.priority = priority;
        this.time = time;
        this.check = check;
        this.status = status;
    }

    public Task(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", note='" + note + '\'' +
                ", priority='" + priority + '\'' +
                ", time='" + time + '\'' +
                ", check=" + check +
                ", status='" + status + '\'' +
                '}';
    }
}
