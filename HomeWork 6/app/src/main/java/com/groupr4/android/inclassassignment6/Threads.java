package com.groupr4.android.inclassassignment6;

import java.io.Serializable;

public class Threads implements Serializable {
    String user_fname, user_lname, title, created_at;
    Integer id, user_id;

    public Threads() {
    }

    public Threads(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
