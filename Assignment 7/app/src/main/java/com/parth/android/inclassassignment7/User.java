/*
 * Copyright (c)
 *  @Group 5
 *  Kshitij Shah - 801077782
 *  Parth Mehta - 801057625
 */

package com.parth.android.inclassassignment7;

import java.io.Serializable;

public class User implements Serializable {
    private String firstName;
    private String lastName;
    private String studentId;
    private int avatarImageValue;
    private String department;

    public User(String firstName, String lastName, String studentId, int avatarImageValue, String department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentId = studentId;
        this.avatarImageValue = avatarImageValue;
        this.department = department;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getAvatarImageValue() {
        return avatarImageValue;
    }

    public void setAvatarImageValue(int avatarImageValue) {
        this.avatarImageValue = avatarImageValue;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
