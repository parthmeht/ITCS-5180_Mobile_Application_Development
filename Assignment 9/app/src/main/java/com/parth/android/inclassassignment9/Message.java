package com.parth.android.inclassassignment9;

import java.io.Serializable;

public class Message implements Serializable {
    private String id;
    private String userId;
    private String message;
    private String uri;
    private String time;
    private String userName;

    public Message(String id, String userId, String message, String time, String userName) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.time = time;
        this.userName = userName;
    }

    public Message(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
