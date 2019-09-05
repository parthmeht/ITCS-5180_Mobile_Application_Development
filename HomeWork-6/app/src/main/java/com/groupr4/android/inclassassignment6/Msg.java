package com.groupr4.android.inclassassignment6;

public class Msg {

    String user_fname, user_lname, user_id, msg_id, createdAt, msgContent;

    public Msg(String user_fname, String user_lname, String user_id, String msg_id, String createdAt, String msgContent) {
        this.user_fname = user_fname;
        this.user_lname = user_lname;
        this.user_id = user_id;
        this.msg_id = msg_id;
        this.createdAt = createdAt;
        this.msgContent = msgContent;
    }

    public Msg() {

    }
}